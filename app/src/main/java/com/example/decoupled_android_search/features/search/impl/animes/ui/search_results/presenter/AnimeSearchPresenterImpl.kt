package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView

class AnimeSearchPresenterImpl(
    val useCase: AnimeSearchUseCase,
    val filter: AnimeFilter
): AnimeSearchPresenter {

    private lateinit var view: AnimeSearchView

    override fun setView(view: AnimeSearchView) {
        this.view = view

    }

    override fun onStart() {
        wrapInsideLoadingAnimation{
            view.updateAnimeList(useCase.get(query = filter.toQuery(), page = 0))
        }
    }

    private fun wrapInsideLoadingAnimation(doOperation: () -> Unit) {
        view.showLoadingAnimation()
        doOperation()
        view.hideLoadingAnimation()
    }
}