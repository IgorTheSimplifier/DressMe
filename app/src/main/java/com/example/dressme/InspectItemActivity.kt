package com.example.dressme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dressme.models.OwnerUser
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_inspect.*
import java.io.File

class InspectItemActivity : AppCompatActivity() {

    companion object {
        val TAG: String = "InspectItemActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspect)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val bundle: Bundle = getIntent().getBundleExtra("item")
        extractBundle(bundle)
        loadImages(bundle)
    }

    private fun loadImages(bundle: Bundle) {
        var bufferFile1: File = File.createTempFile("img2", "jpg")
        var bufferFile2: File = File.createTempFile("img3", "jpg")
        val firebaseRef: FirebaseStorage = FirebaseStorage.getInstance()

        firebaseRef.getReferenceFromUrl(bundle.getString("imageUri")!!)
            .getFile(bufferFile1)
            .addOnSuccessListener {
                Picasso.get()
                    .load(bufferFile1)
                    .placeholder(R.drawable.progress_animation)
                    .into(object_imageView)
            }.addOnFailureListener(OnFailureListener {
            })

        val ownerUser = bundle.getSerializable("owner_user") as OwnerUser
        if (ownerUser.profile_image_uri!=null) {
            firebaseRef.getReferenceFromUrl(ownerUser.profile_image_uri!!)
                .getFile(bufferFile2)
                .addOnSuccessListener {
                    Picasso.get()
                        .load(bufferFile2)
                        .into(seller_image)
                }.addOnFailureListener(OnFailureListener {
                })
        }
    }

    private fun extractBundle(bundle: Bundle) {
        name_textView.text = bundle.getString("name")
        description_textView.text = bundle.getString("description")
        val ownerUser = bundle.getSerializable("owner_user") as OwnerUser
        seller_button.text = ownerUser.name
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
