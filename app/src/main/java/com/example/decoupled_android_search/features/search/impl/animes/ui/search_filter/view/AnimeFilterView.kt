package com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter

interface AnimeFilterView {
    fun updateStatusList(status: List<AnimeQuery.Status>)
    fun updateRatingsList(ratings: List<String>)
    fun notifyRatingsSearchFailure()
    fun updateGenreList(genres: List<String>)
    fun notifyGenresSearchFailure()
    fun showLoadingAnimation()
    fun hideLoadingAnimation()
    fun selectStatus(index: Int)
    fun displaySearchName(name: String)
    fun selectRating(index: Int)
    fun selectGenre(index: Int)
    fun returnFilter(animeFilter: AnimeFilter)
}