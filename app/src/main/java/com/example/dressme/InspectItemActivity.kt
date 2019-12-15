package com.example.dressme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
        Log.d("SANDYBUG", bundle.getString("imageUri"))
        name_textView.text = bundle.getString("name")
        info_textView.text = bundle.getString("info")
        description_textView.text = bundle.getString("description")
        price_textView.text = bundle.getString("price")
        seller_button.text = bundle.getString("seller")

        var bufferFile1: File = File.createTempFile("img2", "jpg")
        var bufferFile2: File = File.createTempFile("img3", "jpg")

        val firebaseRef: FirebaseStorage = FirebaseStorage.getInstance()

        firebaseRef.getReferenceFromUrl(bundle.getString("imageUri"))
            .getFile(bufferFile1)
            .addOnSuccessListener {
                Picasso.get()
                    .load(bufferFile1)
                    .placeholder(R.drawable.progress_animation)
                    .into(object_imageView)
            }.addOnFailureListener(OnFailureListener {
            })

        firebaseRef.getReferenceFromUrl(bundle.getString("sellerImageUri"))
            .getFile(bufferFile2)
            .addOnSuccessListener {
                Picasso.get()
                    .load(bufferFile2)
                    .into(seller_image)
            }.addOnFailureListener(OnFailureListener {
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
