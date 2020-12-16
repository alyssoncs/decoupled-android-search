package com.example.decoupled_android_search.features.search.contract

import android.content.Context
import android.os.Parcelable

interface SearchFactory: Parcelable {
    fun createEmptySearchFilter(): SearchFilter

    fun createSearchFilterIntent(
        context: Context,
        currentFilter: SearchFilter
    ): SearchFilterIntent

    fun createSearchableFragment(
        filter: SearchFilter
    ): SearchContract.SearchableFragment<SearchFilter>
}