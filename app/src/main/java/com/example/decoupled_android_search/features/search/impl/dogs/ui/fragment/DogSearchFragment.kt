package com.example.decoupled_android_search.features.search.impl.dogs.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter

class DogSearchFragment: SearchContract.SearchableFragment<DogFilter>() {

    override fun createSearchableFragment(): SearchContract.SearchableFragment<DogFilter> {
        return DogSearchFragment()
    }

    private lateinit var viewState: DogSearchViewState
    private lateinit var searchFilter: DogFilter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dog_search_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchFilter = getSearchFilter() ?: DogFilter.createEmpty()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewState = ViewModelProvider(this).get(DogSearchViewState::class.java)
    }
}