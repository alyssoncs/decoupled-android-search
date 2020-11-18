package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.core.use_cases.anime_search.Anime
import kotlinx.android.synthetic.main.item_anime.view.animeEpisodes
import kotlinx.android.synthetic.main.item_anime.view.animeImage
import kotlinx.android.synthetic.main.item_anime.view.animeName
import kotlinx.android.synthetic.main.item_anime.view.animeScore

class AnimeAdapter(
    private val context: Context,
    private val animes: MutableList<Anime>
): RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_anime,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, animes[position])
    }

    override fun getItemCount(): Int {
        return animes.size
    }

    fun updateList(newList: List<Anime>) {
        val oldListSize = this.animes.size
        val newListSize = newList.size
        if (newListSize > oldListSize) {
            val newItems = if (oldListSize != 0)
                newList.subList(oldListSize, newListSize)
            else
                newList

            this.animes.addAll(newItems)

            notifyItemRangeInserted(oldListSize, newItems.size)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val animeImage = view.animeImage
        private val animeName = view.animeName
        private val animeEpisodes = view.animeEpisodes
        private val animeScore = view.animeScore

        fun bind(context: Context, anime: Anime) {

            anime.apply {

                Glide.with(context)
                    .load(anime.imageUrl.toString())
                    .into(animeImage)

                animeName.text = name
                animeEpisodes.text = episodes.toString()
                animeScore.text = score.toString()
            }
        }

    }
}
