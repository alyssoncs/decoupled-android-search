package com.example.decoupled_android_search.features.search.impl.dogs.factory

import android.content.Context
import android.content.Intent
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.contract.SearchFactory
import com.example.decoupled_android_search.features.search.contract.SearchFilter
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.activity.DogFilterActivity
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.fragment.DogSearchFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
object DogSearchFactory: SearchFactory {
    override fun createEmptySearchFilter(): SearchFilter {
        return DogFilter.createEmpty()
    }

    override fun createSearchFilterIntent(
        context: Context,
        currentFilter: SearchFilter
    ): Intent {
        return Intent(context, DogFilterActivity::class.java).apply {
            putExtras(currentFilter.toBundle())
        }
    }

    override fun createSearchableFragment(
        filter: SearchFilter
    ): SearchContract.SearchableFragment<SearchFilter> {
        return DogSearchFragment().newInstance(filter)
    }
}