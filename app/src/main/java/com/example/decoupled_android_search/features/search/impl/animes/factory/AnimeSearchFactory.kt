package com.example.decoupled_android_search.features.search.impl.animes.factory

import android.content.Context
import android.content.Intent
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.contract.SearchFactory
import com.example.decoupled_android_search.features.search.contract.SearchFilter
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.activity.AnimeFilterActivity
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.fragment.AnimeSearchFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
class AnimeSearchFactory: SearchFactory {
    override fun createEmptySearchFilter(): SearchFilter {
        return AnimeFilter.createEmpty()
    }

    override fun createSearchFilterIntent(
        context: Context,
        currentFilter: SearchFilter
    ): Intent {
        return Intent(context, AnimeFilterActivity::class.java).apply {
            putExtras(currentFilter.toBundle())
        }
    }

    override fun createSearchableFragment(filter: SearchFilter): SearchContract.SearchableFragment<SearchFilter> {
        return AnimeSearchFragment().newInstance(filter)
    }
}