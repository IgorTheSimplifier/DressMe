package com.example.dressme

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirebaseWrapper {
    companion object {
        // $todo: add loading view
        // $todo: fix: change fragment when uploading will crash user session
        fun uploadItem(
            TAG: String
            , selectedPhotoUri: Uri?
            , name: String
            , descText: String
        ) {
            if (selectedPhotoUri == null) return

            Log.d(TAG, "Uploading photo")

            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

            // Uploading photo first
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d(TAG, "File has been uploaded ${it.metadata?.path}")

                    // If upload succeedded upload item as a transaction
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "Photo download link is in here")
                        uploadItemTransaction(TAG, it.toString(), name, descText)
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
                Log.d(TAG, "[Error] User Owner ID is null")
                return
            }

            db.runTransaction { transaction ->
                Log.d(TAG, "Add item transaction start")

                val snapshot = transaction
                    .get(
                        db.collection("users")
                            .document(userOwnerId)
                    )
                var user: User? = snapshot.toObject(User::class.java)
                Log.d(TAG, "User Successfully desserealised")

                val ownerUserName = user?.name ?: ""
                val ownerUser: OwnerUser = OwnerUser(userOwnerId, ownerUserName)

                val item: Item = Item(name, descText, itemImageUri, ownerUser)

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
                }
                .addOnFailureListener {
                    Log.d(TAG, "Add item transaction failed with error ${it.message}")
                }
        }
    }
}

data class Item(val name: String            =  "",
                val desc_text: String       =  "",
                val item_image_uri: String  ?= null,
                val owner_user: OwnerUser   ?= null)

data class OwnerUser(val user_id: String    = "",
                     val name: String       = "")