package com.example.decoupled_android_search.libraries.navigation

import android.content.Context
import android.content.Intent
import com.example.decoupled_android_search.features.search.contract.SearchFactory
import com.example.decoupled_android_search.features.search.impl.SearchActivity

object Navigation {
    fun getSearchDogsIntent(context: Context, factory: SearchFactory): Intent {
        return Intent(context, SearchActivity::class.java)
            .putExtra(
                SearchActivity.EXTRA_KEY,
                factory
            )
    }

}