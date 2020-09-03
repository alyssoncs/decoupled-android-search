package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.features.search.contract.SearchFilterIntent
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter

class DogFilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_filter)

        val intent = SearchFilterIntent().apply {
            val filter = DogFilter("akita")
            putExtras(filter.toBundle())
        }

        setResult(RESULT_OK, intent)
        finish()
    }
}