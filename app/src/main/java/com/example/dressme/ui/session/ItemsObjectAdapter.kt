package com.example.dressme.ui.session

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dressme.AboutActivity
import com.example.dressme.InspectItemActivity
import com.example.dressme.ProfileMainActivity
import com.example.dressme.R
import com.example.dressme.model.ItemModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.File

class ItemsObjectAdapter: RecyclerView.Adapter<ItemsObjectAdapter.ViewHolder>() {
    var data = listOf<ItemModel>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ItemModel = data[position]
        holder.bind(item)
    }



    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        val seller: TextView = itemView.findViewById(R.id.session_sellerOneName_textView)
        val sellerImage: ImageView = itemView.findViewById(R.id.session_sellerOne_image)
        val objectImage: ImageView = itemView.findViewById(R.id.session_itemOne_image)
        val brand: TextView = itemView.findViewById(R.id.session_itemOneBrand_text)
        val info: TextView = itemView.findViewById(R.id.session_itemOneInfo_text)
        val price: TextView = itemView.findViewById(R.id.session_itemOnePrice_text)
        val rating: TextView = itemView.findViewById(R.id.session_itemOneRating_text)

        fun bind(item: ItemModel) {
            var bufferFile1: File = File.createTempFile("img", "jpg")
            var bufferFile2: File = File.createTempFile("img1", "jpg")

            val storageRef: FirebaseStorage = FirebaseStorage.getInstance()

            storageRef.getReferenceFromUrl(item.imageUri as String)
                .getFile(bufferFile1)
                .addOnSuccessListener {
                    Picasso.get()
                        .load(bufferFile1)
                        .placeholder(R.drawable.progress_animation)
                        .into(objectImage)
            }.addOnFailureListener(OnFailureListener {
                })

            // TODO Refactor Queries
            Log.d("SANDYBUG", item.sellerId.toString())
            val task = FirebaseFirestore.getInstance().collection("sellers")
                .whereEqualTo("sellerId", item.sellerId).get()

            var sellerImageUri: String = ""
            var sellerName: String = ""

            task.addOnSuccessListener {
                // TODO should be unique
                val res = it.getDocuments().get(0)

                sellerImageUri = res.get("profileImageUri") as String
                sellerName = res.get("name") as String

                seller.text = sellerName
                storageRef.getReferenceFromUrl(sellerImageUri)
                    .getFile(bufferFile2)
                    .addOnSuccessListener {
                        Picasso.get()
                            .load(bufferFile2)
                            .into(sellerImage)
                    }.addOnFailureListener(OnFailureListener {
                    })
            }

            objectImage.setOnClickListener{
                var itemBundle: Bundle = prepareBundle(item, sellerImageUri, sellerName)

                val con = itemView.context
                val intent = Intent(con, InspectItemActivity::class.java)
                intent.putExtra("item", itemBundle)
                con.startActivity(intent);
            }

            brand.text = item.brand
            price.text = item.price

            seller.text = "TristanDu92xXx"
            info.text = item.info //"38 / 7.5"
            rating.text = item.rating + "♥"
        }

        // TODO pass query seamlessly
        private fun prepareBundle(item: ItemModel, sellerImageUri: String, sellerName: String): Bundle {
            var itemBundle: Bundle = Bundle()
            itemBundle.putString("name", item.name)
            itemBundle.putString("description", item.description)
            itemBundle.putString("info", item.info)
            itemBundle.putString("brand", item.brand)
            itemBundle.putString("imageUri", item.imageUri)
            itemBundle.putString("price", item.price)
            itemBundle.putLong("sellerId", item.sellerId)
            itemBundle.putString("sellerImageUri", sellerImageUri)
            itemBundle.putString("seller", sellerName)
            return itemBundle
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_sales_object, parent, false)
                return ViewHolder(
                    view
                )
            }
        }
    }
}