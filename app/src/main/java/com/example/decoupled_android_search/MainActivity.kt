package com.example.decoupled_android_search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.decoupled_android_search.features.search.impl.animes.factory.AnimeSearchFactory
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
            val intent = Navigation.getSearchIntent(this, DogSearchFactory())
            startActivity(intent)
        }

        animeSearchButton.setOnClickListener {
            val intent = Navigation.getSearchIntent(this, AnimeSearchFactory())
            startActivity(intent)
        }
    }
}