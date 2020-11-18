package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter.AnimeSearchPresenter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimeSearchPresenterDispatcher(
    private val presenter: AnimeSearchPresenter
): ViewModel(), AnimeSearchPresenter {

    override fun setView(view: AnimeSearchView) {
        presenter.setView(view)
    }

    override fun setFilter(filter: AnimeFilter) {
        presenter.setFilter(filter)
    }

    override fun onStart() = launch {
        presenter.onStart()
    }

    override fun onReachEndOfScroll() = launch {
        presenter.onReachEndOfScroll()
    }

    private fun launch(doOperation: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            doOperation()
        }
    }

    class Factory(
        private val presenter: AnimeSearchPresenter
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(
                AnimeSearchPresenter::class.java
            ).newInstance(presenter)
        }
    }

}
