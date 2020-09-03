package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.decoupled_android_search.R
import kotlinx.android.synthetic.main.item_dog_image.view.*
import java.net.URL

class DogImagesAdapter(
    val context: Context,
    var urlList: MutableList<URL>
): RecyclerView.Adapter<DogImagesAdapter.DogImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogImagesViewHolder {
        return DogImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_dog_image,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return urlList.size
    }

    override fun onBindViewHolder(holder: DogImagesViewHolder, position: Int) {
        holder.bind(context, urlList[position])
    }

    fun updateList(newList: List<URL>) {
        val oldListSize = this.urlList.size
        val newListSize = newList.size
        if (newListSize > oldListSize) {
            val newItems = if (oldListSize != 0)
                newList.subList(oldListSize - 1, newListSize)
            else
                newList

            this.urlList.addAll(newItems)

            notifyItemRangeInserted(oldListSize, newItems.size)
        }
    }

    class DogImagesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.imageView

        fun bind(context: Context, url: URL) {
            Glide.with(context)
                .load(url.toString())
                .into(this.imageView)
        }
    }
}