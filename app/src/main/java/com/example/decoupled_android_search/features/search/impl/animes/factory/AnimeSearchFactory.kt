package com.example.decoupled_android_search.features.search.impl.animes.factory

import android.content.Context
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.contract.SearchFactory
import com.example.decoupled_android_search.features.search.contract.SearchFilterIntent
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.activity.AnimeFilterActivity
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.fragment.AnimeSearchFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
class AnimeSearchFactory: SearchFactory {
    override fun createEmptySearchFilter(): SearchFilterIntent.SearchFilter {
        return AnimeFilter.createEmpty()
    }

    override fun createSearchFilterIntent(
        context: Context,
        currentFilter: SearchFilterIntent.SearchFilter
    ): SearchFilterIntent {
        return SearchFilterIntent().apply {
            setClass(context, AnimeFilterActivity::class.java)
            putExtras(currentFilter.toBundle())
        }
    }

    override fun createSearchableFragment(filter: SearchFilterIntent.SearchFilter): SearchContract.SearchableFragment<SearchFilterIntent.SearchFilter> {
        return AnimeSearchFragment().newInstance(filter)
    }
}