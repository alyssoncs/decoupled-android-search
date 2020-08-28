package com.example.decoupled_android_search.features.search.impl.dogs.ui.view

import java.net.URL

interface DogSearchView {
    fun updateImageList(imagesUrl: List<URL>)
    fun showLoadingAnimation()
    fun hideLoadingAnimation()
    fun notifyInvalidSearchFilter()
    fun displaySearchErrorMessage()
}