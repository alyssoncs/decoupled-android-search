package com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.AnimeFilterView
import com.example.decoupled_android_search.libraries.mvvm_event.MvvmEvent

class AnimeFilterViewModel: ViewModel(), AnimeFilterView {
    private val _statusList: MutableLiveData<List<String>> = MutableLiveData()
    private val _ratings: MutableLiveData<List<String>> = MutableLiveData()
    private val _ratingsSearchHasFailed: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _genres: MutableLiveData<List<String>> = MutableLiveData()
    private val _genresSearchHasFailed: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _statusIndexToSelect: MutableLiveData<MvvmEvent<Int>> = MutableLiveData()
    private val _nameToDisplay: MutableLiveData<MvvmEvent<String>> = MutableLiveData()
    private val _ratingIndexToSelect: MutableLiveData<MvvmEvent<Int>> = MutableLiveData()
    private val _genreIndexToSelect: MutableLiveData<MvvmEvent<Int>> = MutableLiveData()
    private val _animeFilterToReturn: MutableLiveData<MvvmEvent<AnimeFilter>> = MutableLiveData()

    val statusList: LiveData<List<String>> = _statusList
    val ratings: LiveData<List<String>> = _ratings
    val ratingsSearchHasFailed: LiveData<Boolean> = _ratingsSearchHasFailed
    val genres: LiveData<List<String>> = _genres
    val genresSearchHasFailed: LiveData<Boolean> = _genresSearchHasFailed
    val isLoading: LiveData<Boolean> = _isLoading
    val statusIndexToSelect: LiveData<MvvmEvent<Int>> = _statusIndexToSelect
    val nameToDisplay: LiveData<MvvmEvent<String>> = _nameToDisplay
    val ratingIndexToSelect: LiveData<MvvmEvent<Int>> = _ratingIndexToSelect
    val genreIndexToSelect: LiveData<MvvmEvent<Int>> = _genreIndexToSelect
    val animeFilterToReturn: LiveData<MvvmEvent<AnimeFilter>> = _animeFilterToReturn

    override fun updateStatusList(status: List<AnimeQuery.Status>) {
        _statusList.postValue(status.map { it.name })
    }

    override fun updateRatingsList(ratings: List<String>) {
        _ratings.postValue(ratings)
    }

    override fun notifyRatingsSearchFailure() {
        _ratingsSearchHasFailed.postValue(true)
    }

    override fun updateGenreList(genres: List<String>) {
        _genres.postValue(genres)
    }

    override fun notifyGenresSearchFailure() {
        _genresSearchHasFailed.postValue(true)
    }

    override fun showLoadingAnimation() {
        _isLoading.postValue(true)
    }

    override fun hideLoadingAnimation() {
        _isLoading.postValue(false)
    }

    override fun selectStatus(index: Int) {
        _statusIndexToSelect.postValue(MvvmEvent(index))
    }

    override fun displaySearchName(name: String) {
        _nameToDisplay.postValue(MvvmEvent(name))
    }

    override fun selectRating(index: Int) {
        _ratingIndexToSelect.postValue(MvvmEvent(index))
    }

    override fun selectGenre(index: Int) {
        _genreIndexToSelect.postValue(MvvmEvent(index))
    }

    override fun returnFilter(animeFilter: AnimeFilter) {
        _animeFilterToReturn.postValue(MvvmEvent(animeFilter))
    }
}
