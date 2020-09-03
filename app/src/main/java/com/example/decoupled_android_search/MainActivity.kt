package com.example.decoupled_android_search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.decoupled_android_search.features.search.impl.SearchActivity
import com.example.decoupled_android_search.features.search.impl.dogs.factory.DogSearchFactory
import com.example.decoupled_android_search.libraries.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        init()
    }

    private fun init() {
        setSupportActionBar(toolbar)
        searchButton.setOnClickListener {
            val intent = Navigation.getSearchDogsIntent(this, DogSearchFactory())
            startActivity(intent)
        }
    }
}