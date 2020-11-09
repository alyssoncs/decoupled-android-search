package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view

import com.example.decoupled_android_search.core.use_cases.anime_search.Anime

interface AnimeSearchView {
    fun setAppBarTitle(title: String)
    fun notifyInvalidFilter()
    fun showLoadingAnimation()
    fun hideLoadingAnimation()
    fun updateAnimeList(list: List<Anime>)
}

