package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view

import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter

interface DogFilterView {
    fun showLoadingAnimation()
    fun hideLoadingAnimation()
    fun setBreedSelectionOptions(breeds: List<String>)
    fun notifyBreedSearchError()
    fun setSubBreedSelectionOptions(subBreeds: List<String>)
    fun notifySubBreedSearchError()
    fun clearSubBreedOptions()
    fun returnSearchFilter(dogFilter: DogFilter)
}
