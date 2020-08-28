package com.example.decoupled_android_search.features.search.impl.dogs.ui.presenter

import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.view.DogSearchView

interface DogSearchPresenter {
    fun setView(view: DogSearchView)
    fun onStart()
    fun onReachEndOfScroll()
}