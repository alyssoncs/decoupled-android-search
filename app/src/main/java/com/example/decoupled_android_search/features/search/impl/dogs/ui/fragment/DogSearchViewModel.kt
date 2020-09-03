package com.example.decoupled_android_search.features.search.impl.dogs.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.decoupled_android_search.features.search.impl.dogs.ui.view.DogSearchView
import com.example.decoupled_android_search.libraries.mvvm_event.MvvmEvent
import java.net.URL

fun <T> MutableLiveData<T>.notifyObserver() {
    this.postValue(this.value)
}

class DogSearchViewModel : ViewModel(), DogSearchView {
    private val _imageList = MutableLiveData(mutableListOf<URL>())
    private val _shouldShowLoadingAnimation = MutableLiveData(false)
    private val _shouldShowInvalidSearchFilter = MutableLiveData(MvvmEvent(false))
    private val _shouldShowSearchErrorMessage = MutableLiveData(MvvmEvent(false))
    private val _appBarTitle = MutableLiveData<String>()

    val imageList: LiveData<MutableList<URL>> = _imageList
    val shouldShowLoadingAnimation: LiveData<Boolean> = _shouldShowLoadingAnimation
    val shouldShowInvalidSearchFilter: LiveData<MvvmEvent<Boolean>> = _shouldShowInvalidSearchFilter
    val shouldShowSearchErrorMessage : LiveData<MvvmEvent<Boolean>> = _shouldShowSearchErrorMessage
    val appBarTitle: LiveData<String> = _appBarTitle

    override fun updateImageList(imagesUrl: List<URL>) {
        _imageList.value?.addAll(imagesUrl)
        _imageList.notifyObserver()
    }

    override fun showLoadingAnimation() {
        _shouldShowLoadingAnimation.postValue(true)
    }

    override fun hideLoadingAnimation() {
        _shouldShowLoadingAnimation.postValue(false)
    }

    override fun notifyInvalidSearchFilter() {
        _shouldShowInvalidSearchFilter.postValue(MvvmEvent(true))
    }

    override fun displaySearchErrorMessage() {
        _shouldShowSearchErrorMessage.postValue(MvvmEvent(true))
    }

    override fun changeAppBarTitle(title: String) {
        _appBarTitle.postValue(title)
    }

}