package com.example.decoupled_android_search.features.search.impl.dogs.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.decoupled_android_search.features.search.impl.dogs.ui.presenter.DogSearchPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.view.DogSearchView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DogSearchPresenterDispatcher(
    private val presenter: DogSearchPresenter
): ViewModel(), DogSearchPresenter {

    override fun setView(view: DogSearchView) {
        presenter.setView(view)
    }

    override fun onStart() {
        launch {
            presenter.onStart()
        }
    }

    override fun onReachEndOfScroll() {
        launch {
            presenter.onReachEndOfScroll()
        }
    }

    private fun launch(doOperation: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            doOperation()
        }
    }

    class Factory(
        private val presenter: DogSearchPresenter
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(
                DogSearchPresenter::class.java
            ).newInstance(presenter)
        }
    }
}
