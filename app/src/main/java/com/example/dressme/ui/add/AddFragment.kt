package com.example.dressme.ui.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dressme.R
import com.example.dressme.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val TAG: String = "AddFragment"

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    private lateinit var addViewModel: AddViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addViewModel =
            ViewModelProviders.of(this).get(AddViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add, container, false)
        addViewModel.text.observe(this, Observer {
        })

        root.confirm_button_add.setOnClickListener {
            confirm()
        }

        root.add_img_button_add.setOnClickListener {
            selectImageFromGalery()
        }

        return root
    }


    private fun confirm() {
        // $todo: add proper notification
        if(!valid()) {
            Toast.makeText(getActivity(), "Form cannot be validated", Toast.LENGTH_LONG).show()
            return
        }

        val name : String = title_edittext_add.text.toString()
        val desc_text : String = item_desc_edittext_add.text.toString()

        // $todo: take a look at edge case
        val user_owner_id   = FirebaseAuth.getInstance().uid ?: ""
        val db              = FirebaseFirestore.getInstance()

        db.runTransaction { transaction ->
            Log.d(TAG, "Add item transaction beggins")
            val snapshot    = transaction.get(db.collection("users").document(user_owner_id))
            var user: User? = snapshot.toObject(User::class.java)
            Log.d(TAG, "Successfully desserealised")

            val ownerUserName           = user?.name ?: ""
            val ownerUser: OwnerUser    = OwnerUser(user_owner_id, ownerUserName)
            val item: Item              = Item(name, desc_text, ownerUser)

            val newItemRef = db.collection("items").document()

            transaction.set(newItemRef, item)
            // Kostil
            transaction.update(db.collection("users").document(user_owner_id)
                , "name"
                , ownerUserName)
        }
            .addOnSuccessListener {
                Log.d(TAG, "Add item transaction went successfully")
                clearForm()
            }
            .addOnFailureListener {
                Log.d(TAG, "Add item transaction failed with erre ${it.message}")
            }
    }


    private fun selectImageFromGalery() {
        Log.d( TAG, "selecting image")

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE
            && resultCode == Activity.RESULT_OK
            && data != null)
        {
            Log.d(TAG, "Photo was sellected")

            image_view_add.setImageURI(data?.data)
        }

    }


    // $todo: update validation
    private fun valid(): Boolean {
        val name       = title_edittext_add.text.toString()
        val desc_text   = item_desc_edittext_add.text.toString()

        if (name.isEmpty() || desc_text.isEmpty())
            return false
        return true
    }


    private fun clearForm() {
        title_edittext_add.text.clear()
        item_desc_edittext_add.text.clear()
        image_view_add.setImageResource(0)
    }
}


data class Item(val name: String            =  "",
                val desc_text: String       =  "",
                val owner_user: OwnerUser   ?= null)

data class OwnerUser(val user_id: String    = "",
                     val name: String       = "")