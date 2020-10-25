package com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.presenter

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.AnimeFilterView

class AnimeFilterPresenterImpl(
    private val useCase: AnimeSearchUseCase
) : AnimeFilterPresenter {

    private lateinit var view: AnimeFilterView
    private lateinit var currentFilter: AnimeFilter

    private var statusList: List<AnimeQuery.Status> = emptyList()
    private var ratings: List<AnimeQuery.Rating> = emptyList()
    private var genres: List<AnimeQuery.Genre> = emptyList()

    override fun setView(view: AnimeFilterView) {
        this.view = view
    }

    override fun onStart(filter: AnimeFilter) {
        saveCurrentFilter(filter)
        fetchDataFromUseCase()
        configureViewAccordinglyToCurrentFilter()
    }

    override fun onAnimeNameChanged(animeName: String) {
        currentFilter = currentFilter.copy(name = animeName)
    }

    override fun onStatusSelected(index: Int) {
        if (index in statusList.indices)
            currentFilter = currentFilter.copy(status = statusList[index])
    }

    override fun onStatusUnselected() {
        currentFilter = currentFilter.copy(status = null)
    }

    override fun onRatingSelected(index: Int) {
        if (index in ratings.indices) {
            val rating = AnimeFilter.Rating(ratings[index])
            currentFilter = currentFilter.copy(rating = rating)
        }
    }

    override fun onRatingUnselected() {
        currentFilter = currentFilter.copy(rating = null)
    }

    override fun onGenreSelected(index: Int) {
        if (index in genres.indices) {
            val genre = AnimeFilter.Genre(genres[index])
            currentFilter = currentFilter.copy(genre = genre)
        }
    }

    override fun onGenreUnselected() {
        currentFilter = currentFilter.copy(genre = null)
    }

    override fun onConfirmButtonClick() {
        view.returnFilter(currentFilter)
    }

    private fun saveCurrentFilter(filter: AnimeFilter) {
        currentFilter = filter
    }

    private fun fetchDataFromUseCase() {
        wrapInsideLoadingAnimation {
            updateStatusList()
            updateRatingsList()
            updateGenreList()
        }
    }

    private fun configureViewAccordinglyToCurrentFilter() {
        displaySearchName()
        displayStatus()
        displayRating()
        displayGenre()
    }

    private fun wrapInsideLoadingAnimation(doOperation: () -> Unit) {
        view.showLoadingAnimation()
        doOperation()
        view.hideLoadingAnimation()
    }

    private fun updateStatusList() {
        this.statusList = useCase.getStatus()
        view.updateStatusList(statusList)
    }

    private fun updateRatingsList() {
        try {
            this.ratings = useCase.getRatings()
            view.updateRatingsList(ratings.map { it.name })
        } catch (e: AnimeSearchUseCase.SearchException) {
            view.notifyRatingsSearchFailure()
        }
    }

    private fun updateGenreList() {
        try {
            this.genres = useCase.getGenres()
            view.updateGenreList(genres.map { it.name })
        } catch (e: AnimeSearchUseCase.SearchException) {
            view.notifyGenresSearchFailure()
        }
    }

    private fun displaySearchName() {
        if (!currentFilter.isEmpty())
            view.displaySearchName(currentFilter.name)
    }

    private fun displayStatus() {
        if (statusList.contains(currentFilter.status))
            view.selectStatus(statusList.indexOf(currentFilter.status))
    }

    private fun displayRating() {
        ratings.map { it.id }.apply {
            if (contains(currentFilter.rating?.ratingId))
                view.selectRating(indexOf(currentFilter.rating?.ratingId))
        }
    }

    private fun displayGenre() {
        genres.map { it.id }.apply {
            if (contains(currentFilter.genre?.genreId))
                view.selectGenre(indexOf(currentFilter.genre?.genreId))
        }
    }
}