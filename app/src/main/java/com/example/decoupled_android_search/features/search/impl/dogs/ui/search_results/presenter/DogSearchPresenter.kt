package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.presenter

import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.DogSearchView

interface DogSearchPresenter {
    fun setView(view: DogSearchView)
    fun onStart()
    fun onReachEndOfScroll()
}