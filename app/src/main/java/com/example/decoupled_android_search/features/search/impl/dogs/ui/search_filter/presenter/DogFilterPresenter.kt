package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter

import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.DogFilterView

interface DogFilterPresenter {
    fun setView(view: DogFilterView)
    fun onStart()
    fun onBreedSelected(index: Int)
    fun onBreedDeselect()
    fun onSubBreedSelected(index: Int)
    fun onSubBreedDeselected()
    fun onSubmitButtonClick()
}