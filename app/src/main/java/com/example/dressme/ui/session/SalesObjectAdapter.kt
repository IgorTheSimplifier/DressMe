package com.example.dressme.ui.session

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dressme.R
import com.example.dressme.sandbox.SalesObject

class SalesObjectAdapter: RecyclerView.Adapter<SalesObjectAdapter.ViewHolder>() {
//    var data = listOf<SalesObject>()
//    set(value) {
//        field = value
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int = 5 //data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = data[position]
//        holder.bind(item)
        // TODO hello
    }



    class ViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tex: TextView = itemView.findViewById(R.id.session_sellerOneName_textView)
        fun bind(item: SalesObject) {
            val res = itemView.context.resources

            tex.text = "Hello World!"

            TODO("Complete the dynamic retrieval of items")
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