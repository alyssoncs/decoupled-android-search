package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.presenter

import com.example.decoupled_android_search.core.use_cases.dog_search.Breed
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchUseCase
import com.example.decoupled_android_search.core.use_cases.dog_search.SubBreed
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.DogSearchView
import java.net.URL

class DogSearchPresenterImpl(
    private val useCase: DogSearchUseCase,
    private var filter: DogFilter
): DogSearchPresenter {
    private lateinit var view: DogSearchView
    private var nextPageToFetch = 0

    override fun setView(view: DogSearchView) {
        this.view = view
    }

    override fun onStart() {
        val title = getFormattedTitle()
        view.changeAppBarTitle(title)


        wrapInsideLoadingAnimation {
            if (filter.isEmpty())
                view.notifyInvalidSearchFilter()
            else
                fetchAndDisplayNextPageOfImages()
        }
    }

    override fun onReachEndOfScroll() {
        wrapInsideLoadingAnimation {
            fetchAndDisplayNextPageOfImages()
        }
    }

    private fun getFormattedTitle(): String {
        return if (filter.hasSubBreed())
            "${filter.subBreed} ${filter.breed}"
        else
            filter.breed
    }

    private fun wrapInsideLoadingAnimation(doOperation: () -> Unit) {
        view.showLoadingAnimation()
        doOperation()
        view.hideLoadingAnimation()
    }

    private fun fetchAndDisplayNextPageOfImages() {
        try {
            tryToUpdateImageList(nextPageToFetch)
            nextPageToFetch++
        } catch (e: DogSearchUseCase.SearchException) {
            view.displaySearchErrorMessage()
        }
    }

    private fun tryToUpdateImageList(page: Int) {
        val images = getDogImages(page)
        view.updateImageList(images)
    }

    private fun getDogImages(page: Int): List<URL> {
        return if (filter.hasSubBreed())
            getSubBreedImages(page)
        else
            getBreedImages(page)
    }

    private fun getBreedImages(page: Int): List<URL> {
        val breed = Breed(filter.breed)
        return useCase.getImages(breed, page)
    }

    private fun getSubBreedImages(page: Int): List<URL> {
        val breed = Breed(filter.breed)
        val subBreed = SubBreed(breed, filter.subBreed)
        return useCase.getImages(subBreed, page)
    }

}