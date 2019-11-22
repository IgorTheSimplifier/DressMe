package com.example.dressme.ui.add

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.example.dressme.FirebaseWrapper
import com.example.dressme.R
import com.example.dressme.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.util.*

class AddFragment : Fragment() {

    private val TAG: String = "AddFragment"

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
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

        val name: String        = title_edittext_add.text.toString()
        val descText: String    = item_desc_edittext_add.text.toString()
        FirebaseWrapper.uploadItem(TAG, selectedPhotoUri, name, descText)
        cleanup()
    }


    var selectedPhotoUri: Uri? = null

    private fun selectImageFromGalery() {
        Log.d( TAG, "Selecting image")

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
            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data?.data
            image_view_add.setImageURI(selectedPhotoUri)
        }
    }

    private fun valid(): Boolean {
        val name       = title_edittext_add.text.toString()
        val descText   = item_desc_edittext_add.text.toString()

        if (name.isEmpty() || descText.isEmpty() || selectedPhotoUri == null)
            return false
        return true
    }

    private fun cleanup() {
        title_edittext_add.text.clear()
        item_desc_edittext_add.text.clear()
        image_view_add.setImageResource(0)
    }
}