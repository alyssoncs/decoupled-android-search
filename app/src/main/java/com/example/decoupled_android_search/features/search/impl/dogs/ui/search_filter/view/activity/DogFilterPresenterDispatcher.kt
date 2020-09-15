package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter.DogFilterPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.DogFilterView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DogFilterPresenterDispatcher(
    private val presenter: DogFilterPresenter
): ViewModel(), DogFilterPresenter {
    override fun setView(view: DogFilterView) {
        presenter.setView(view)
    }

    override fun onStart() {
        launch {
            presenter.onStart()
        }
    }

    override fun onBreedSelected(index: Int) {
        launch {
            presenter.onBreedSelected(index)
        }
    }

    override fun onBreedDeselect() {
        launch {
            presenter.onBreedDeselect()
        }
    }

    override fun onSubBreedSelected(index: Int) {
        launch {
            presenter.onSubBreedSelected(index)
        }
    }

    override fun onSubBreedDeselected() {
        launch {
            presenter.onSubBreedDeselected()
        }
    }

    override fun onSubmitButtonClick() {
        launch {
            presenter.onSubmitButtonClick()
        }
    }

    private fun launch(doOperation: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            doOperation()
        }
    }

    class Factory(
        val presenter: DogFilterPresenter
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(
                DogFilterPresenter::class.java
            ).newInstance(presenter)
        }

    }
}