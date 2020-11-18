package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView

class AnimeSearchPresenterImpl(
    private val useCase: AnimeSearchUseCase,
): AnimeSearchPresenter {

    private lateinit var view: AnimeSearchView
    private lateinit var filter: AnimeFilter

    private var nextPageToFetch: Int = 0

    override fun setView(view: AnimeSearchView) {
        this.view = view
    }

    override fun setFilter(filter: AnimeFilter) {
        this.filter = filter
    }

    override fun onStart() {
        view.setAppBarTitle(filter.name)

        wrapInsideLoadingAnimation{
            if (filter.isEmpty())
                view.notifyInvalidFilter()
            else
                fetchNextPageAndUpdateList()
        }
    }

    override fun onReachEndOfScroll() {
        if (!filter.isEmpty() && nextPageToFetch > 0) {
            wrapInsideLoadingAnimation {
                fetchNextPageAndUpdateList()
            }
        }
    }

    private fun fetchNextPageAndUpdateList() {
        try {
            val animeList = useCase.get(query = filter.toQuery(), page = nextPageToFetch)
            if (animeList.isNotEmpty()) {
                view.updateAnimeList(animeList)
                nextPageToFetch++
            } else {
                nextPageToFetch = -1
            }
        } catch (e: AnimeSearchUseCase.SearchException) {
            view.displaySearchErrorMessage()
        }
    }

    private fun wrapInsideLoadingAnimation(doOperation: () -> Unit) {
        view.showLoadingAnimation()
        doOperation()
        view.hideLoadingAnimation()
    }
}