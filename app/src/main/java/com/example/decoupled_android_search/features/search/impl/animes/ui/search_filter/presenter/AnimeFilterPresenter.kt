package com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.presenter

import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.AnimeFilterView

interface AnimeFilterPresenter {
    fun setView(view: AnimeFilterView)
    fun onStart(filter: AnimeFilter = AnimeFilter.createEmpty())
    fun onAnimeNameChanged(animeName: String)
    fun onStatusSelected(index: Int)
    fun onStatusUnselected()
    fun onRatingSelected(index: Int)
    fun onRatingUnselected()
    fun onGenreSelected(index: Int)
    fun onGenreUnselected()
    fun onConfirmButtonClick()
}