package com.example.decoupled_android_search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.activity.AnimeFilterActivity
import com.example.decoupled_android_search.features.search.impl.dogs.factory.DogSearchFactory
import com.example.decoupled_android_search.libraries.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.animeSearchButton
import kotlinx.android.synthetic.main.activity_main.dogSearchButton
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        init()
    }

    private fun init() {
        setSupportActionBar(toolbar)

        dogSearchButton.setOnClickListener {
            val intent = Navigation.getSearchDogsIntent(this, DogSearchFactory())
            startActivity(intent)
        }

        animeSearchButton.setOnClickListener {
            val filter = AnimeFilter(
                name = "naruto",
                status = AnimeQuery.Status.AIRING,
                rating = AnimeFilter.Rating(AnimeQuery.Rating("all ages", "all")),
                genre = AnimeFilter.Genre(AnimeQuery.Genre("action", ""))
            )

            val intent = Intent(this, AnimeFilterActivity::class.java).apply {
                putExtras(filter.toBundle())
            }
            //val intent = Navigation.getSearchDogsIntent(this, AnimeSearchFactory())
            startActivity(intent)
        }
    }
}