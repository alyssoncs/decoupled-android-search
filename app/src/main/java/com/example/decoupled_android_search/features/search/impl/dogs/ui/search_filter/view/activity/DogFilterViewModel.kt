package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.DogFilterView
import com.example.decoupled_android_search.libraries.mvvm_event.MvvmEvent

class DogFilterViewModel: ViewModel(), DogFilterView {
    private val _shouldShowLoadingAnimation = MutableLiveData(false)
    private val _breedList = MutableLiveData(listOf<String>())
    private val _notifyBreedSearchError = MutableLiveData(MvvmEvent(false))
    private val _subBreedList = MutableLiveData(listOf<String>())
    private val _notifySubBreedSearchError = MutableLiveData(MvvmEvent(false))
    private val _returnSelection = MutableLiveData(MvvmEvent(ReturnSelectionEvent(DogFilter.createEmpty(), false)))

    val shouldShowLoadingAnimation: LiveData<Boolean> = _shouldShowLoadingAnimation
    val breedList: LiveData<List<String>> = _breedList
    val notifyBreedSearchError: LiveData<MvvmEvent<Boolean>> = _notifyBreedSearchError
    val subBreedList: LiveData<List<String>> = _subBreedList
    val notifySubBreedSearchError: LiveData<MvvmEvent<Boolean>> = _notifySubBreedSearchError
    val returnSelection: LiveData<MvvmEvent<ReturnSelectionEvent>> = _returnSelection

    override fun showLoadingAnimation() {
        _shouldShowLoadingAnimation.postValue(true)
    }

    override fun hideLoadingAnimation() {
        _shouldShowLoadingAnimation.postValue(false)
    }

    override fun setBreedSelectionOptions(breeds: List<String>) {
        _breedList.postValue(breeds)
    }

    override fun notifyBreedSearchError() {
        _notifyBreedSearchError.postValue(MvvmEvent(true))
    }

    override fun setSubBreedSelectionOptions(subBreeds: List<String>) {
        _subBreedList.postValue(subBreeds)
    }

    override fun notifySubBreedSearchError() {
        _notifySubBreedSearchError.postValue(MvvmEvent(true))
    }

    override fun clearSubBreedOptions() {
        _breedList.postValue(emptyList())
    }

    override fun returnSearchFilter(dogFilter: DogFilter) {
        val event = ReturnSelectionEvent(dogFilter, true)
        _returnSelection.postValue(MvvmEvent(event))
    }

    data class ReturnSelectionEvent(
        val dogFilter: DogFilter,
        val shouldReturn: Boolean
    )
}