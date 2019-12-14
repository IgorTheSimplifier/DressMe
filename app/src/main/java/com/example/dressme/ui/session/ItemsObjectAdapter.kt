package com.example.dressme.ui.session

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dressme.InspectItemActivity
import com.example.dressme.R
import com.example.dressme.model.ItemModel
import com.google.android.gms.tasks.OnFailureListener
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
            var bufferFile: File = File.createTempFile("img", "jpg")

            Log.d("DEBUG STRING", FirebaseStorage.getInstance().toString())
            Log.d("DEBUG STRING", item.toString())

            val dbRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(
                item.image_uri as String)

            dbRef.getFile(bufferFile)
                .addOnSuccessListener {
                    Picasso.get()
                        .load(bufferFile)
                        .placeholder(R.drawable.progress_animation)
                        .into(objectImage)
            }.addOnFailureListener(OnFailureListener {
                })

//            objectImage.setOnClickListener{
//                val intent = Intent(context, InspectItemActivity::class.java)
//                startActivity(intent);
//            }

            brand.text = item.brand
            price.text = item.price

            seller.text = "TristanDu92xXx"
            info.text = "38 / 7.5"
            rating.text = "10♥"
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