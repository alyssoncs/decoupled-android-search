package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter

import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView

interface AnimeSearchPresenter {
    fun setView(view: AnimeSearchView)
    fun setFilter(filter: AnimeFilter)
    fun onStart()
    fun onReachEndOfScroll()
}