package com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.presenter.AnimeFilterPresenter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.AnimeFilterView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimeFilterPresenterDispatcher(
    private val presenter: AnimeFilterPresenter
): ViewModel(), AnimeFilterPresenter {

    override fun setView(view: AnimeFilterView) {
        presenter.setView(view)
    }

    override fun onStart(filter: AnimeFilter) = launch {
        presenter.onStart(filter)
    }

    override fun onAnimeNameChanged(animeName: String) = launch {
        presenter.onAnimeNameChanged(animeName)
    }

    override fun onStatusSelected(index: Int) = launch {
        presenter.onStatusSelected(index)
    }

    override fun onStatusUnselected() = launch {
        presenter.onStatusUnselected()
    }

    override fun onRatingSelected(index: Int) = launch {
        presenter.onRatingSelected(index)
    }

    override fun onRatingUnselected() = launch {
        presenter.onRatingUnselected()
    }

    override fun onGenreSelected(index: Int) = launch {
        presenter.onGenreSelected(index)
    }

    override fun onGenreUnselected() = launch {
        presenter.onGenreUnselected()

    }

    override fun onConfirmButtonClick() = launch {
        presenter.onConfirmButtonClick()
    }

    private fun launch(doOperation: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            doOperation()
        }
    }

    class Factory(
        val presenter: AnimeFilterPresenter
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(
                AnimeFilterPresenter::class.java
            ).newInstance(presenter)
        }
    }
}