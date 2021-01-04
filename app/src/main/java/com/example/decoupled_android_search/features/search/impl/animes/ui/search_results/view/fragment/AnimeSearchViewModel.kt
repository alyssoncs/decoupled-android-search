package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.decoupled_android_search.core.entities.Anime
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView
import com.example.decoupled_android_search.libraries.mvvm_event.MvvmEvent

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}

class AnimeSearchViewModel: ViewModel(), AnimeSearchView {
    private val _appBarTitle = MutableLiveData<String>()
    private val _animeList = MutableLiveData(mutableListOf<Anime>())
    private val _shouldShowLoadingAnimation = MutableLiveData(false)
    private val _shouldShowSearchErrorMessage = MutableLiveData(MvvmEvent(false))
    private val _shouldShowInvalidSearchFilter = MutableLiveData(MvvmEvent(false))

    val appBarTitle: LiveData<String> = _appBarTitle
    val animeList: LiveData<MutableList<Anime>> = _animeList
    val shouldShowLoadingAnimation: LiveData<Boolean> = _shouldShowLoadingAnimation
    val shouldShowSearchErrorMessage: LiveData<MvvmEvent<Boolean>> = _shouldShowSearchErrorMessage
    val shouldShowInvalidSearchFilter: LiveData<MvvmEvent<Boolean>> = _shouldShowInvalidSearchFilter

    override fun setAppBarTitle(title: String) {
        _appBarTitle.postValue(title)
    }

    override fun notifyInvalidFilter() {
        _shouldShowInvalidSearchFilter.postValue(MvvmEvent(true))
    }

    override fun showLoadingAnimation() {
        _shouldShowLoadingAnimation.postValue(true)
    }

    override fun hideLoadingAnimation() {
        _shouldShowLoadingAnimation.postValue(false)
    }

    override fun updateAnimeList(list: List<Anime>) {
        _animeList.value?.addAll(list)
        _animeList.notifyObserver()
    }

    override fun displaySearchErrorMessage() {
        _shouldShowSearchErrorMessage.postValue(MvvmEvent(true))
    }
}
