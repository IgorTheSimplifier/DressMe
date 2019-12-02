package com.example.dressme.ui.add

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dressme.R
import com.example.dressme.models.Item
import com.example.dressme.models.OwnerUser
import com.example.dressme.models.UserAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import dmax.dialog.SpotsDialog
import java.util.*

class AddFragment : Fragment() {

    companion object {
        val TAG: String = "AddFragment"
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
    }

    private lateinit var addViewModel: AddViewModel

    private lateinit var alertDialog: AlertDialog

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

        alertDialog = SpotsDialog.Builder()
            .setContext(context)
            .setMessage("Uploading")
            .setCancelable(false)
            .build()

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
        if(!validForm()) {
            Toast.makeText(getActivity(), "Form cannot be validated", Toast.LENGTH_LONG).show()
            return
        }

        val name: String        = title_edittext_add.text.toString()
        val descText: String    = item_desc_edittext_add.text.toString()

        alertDialog.show()
        uploadItem(TAG, selectedPhotoUri, name, descText)
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

            selectedPhotoUri = data.data
            image_view_add.setImageURI(selectedPhotoUri)
        }
    }

    private fun validForm(): Boolean {
        val name       = title_edittext_add.text.toString()
        val descText   = item_desc_edittext_add.text.toString()

        if (name.isEmpty() || descText.isEmpty() || selectedPhotoUri == null)
            return false
        return true
    }

    private fun cleanup() {
        title_edittext_add.text.clear()
        item_desc_edittext_add.text.clear()
        image_view_add.setImageResource(R.drawable.ic_account)
    }


    private fun uploadItem(TAG: String
                            , selectedPhotoUri: Uri?
                            , name: String
                            , descText: String) {
        if (selectedPhotoUri == null) return

        Log.d(TAG, "Uploading photo")

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        // Uploading photo first
        ref.putFile(selectedPhotoUri)
            .addOnSuccessListener {
                Log.d(TAG, "File has been uploaded ${it.metadata?.path}")

                // If upload succeedded upload item as a transaction
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "Photo download link is in here")
                    uploadItemTransaction(
                        TAG,
                        it.toString(),
                        name,
                        descText
                    )
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Uploading has failed to upload with exception ${it.toString()}")
            }
            .addOnCanceledListener {
                Log.d(TAG, "Uploading has been interrupted to upload with exception")
            }
    }

    private fun uploadItemTransaction(
        TAG: String
        , itemImageUri: String
        , name: String
        , descText: String
    ) {
        Log.d(TAG, "Uploading item to Firestore")


        val userOwnerId = FirebaseAuth.getInstance().uid ?: ""
        val db = FirebaseFirestore.getInstance()

        if (userOwnerId == "") {
            Log.d(TAG, "[Error] UserAuth Owner ID is null")
            return
        }

        db.runTransaction { transaction ->
            Log.d(TAG, "Add item transaction start")

            val snapshot = transaction
                .get(
                    db.collection("users")
                        .document(userOwnerId)
                )
            var user: UserAuth? = snapshot.toObject(UserAuth::class.java)
            Log.d(TAG, "UserAuth Successfully desserealised")

            val ownerUserName = user?.name ?: ""
            val ownerUser: OwnerUser =
                OwnerUser(userOwnerId, ownerUserName)

            val item: Item =
                Item(name, descText, itemImageUri, ownerUser)

            val newItemRef = db.collection("items").document()

            transaction.set(newItemRef, item)
            transaction.update(
                db.collection("users").document(userOwnerId)
                , "name"
                , ownerUserName
            )
        }
            .addOnSuccessListener {
                Log.d(TAG, "Add item transaction went successfully")
                alertDialog.dismiss()
                cleanup()
            }
            .addOnFailureListener {
                Log.d(TAG, "Add item transaction failed with error ${it.message}")
            }
    }
}