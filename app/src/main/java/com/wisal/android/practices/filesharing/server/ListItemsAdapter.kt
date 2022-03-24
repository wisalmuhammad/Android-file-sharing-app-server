package com.wisal.android.practices.filesharing.server

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class RowItem(val name: String,val path: Uri)

class ListItemsAdapter: RecyclerView.Adapter<ListItemsAdapter.ViewHolder>() {

    private val itemsList: MutableList<RowItem> = mutableListOf()
    private var callback : ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_layout,parent,false)
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemsList[position])
        holder.rootView.setOnClickListener {
            callback?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun submitData(items: List<RowItem>) {
        itemsList.addAll(items)
    }

    fun addItemClickHandler(callback: (Int) -> Unit) {
        this.callback = callback
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val nameView = view.findViewById<TextView>(R.id.name_view)
        private val imageView = view.findViewById<ImageView>(R.id.image_view)
        val rootView  = view.findViewById<MaterialCardView>(R.id.root_view)

        fun bind(item: RowItem) {
            Log.d("Debug","Image file path: ${item.path}")
            nameView.text = item.name
            Glide.with(imageView.context)
                .load(item.path)
                .centerCrop()
                .into(imageView)
        }
    }
}