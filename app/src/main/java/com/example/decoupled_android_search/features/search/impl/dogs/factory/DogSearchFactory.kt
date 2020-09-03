package com.example.decoupled_android_search.features.search.impl.dogs.factory

import android.content.Context
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.contract.SearchFactory
import com.example.decoupled_android_search.features.search.contract.SearchFilterIntent
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.activity.DogFilterActivity
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.fragment.DogSearchFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
class DogSearchFactory: SearchFactory {
    override fun createEmptySearchFilter(): SearchFilterIntent.SearchFilter {
        return DogFilter.createEmpty()
    }

    override fun createSearchFilterIntent(
        context: Context,
        currentFilter: SearchFilterIntent.SearchFilter
    ): SearchFilterIntent {
        return SearchFilterIntent().apply {
            setClass(context, DogFilterActivity::class.java)
            putExtras(currentFilter.toBundle())
        }
    }

    override fun createSearchableFragment(
        filter: SearchFilterIntent.SearchFilter
    ): SearchContract.SearchableFragment<SearchFilterIntent.SearchFilter> {
        return DogSearchFragment().newInstance(filter)
    }
}