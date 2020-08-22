package com.example.decoupled_android_search.features.search.contract

import android.content.Context
import android.os.Parcelable

interface SearchFactory: Parcelable {
    fun createEmptySearchFilter(): SearchFilterIntent.SearchFilter
    fun createSearchFilterIntent(context: Context, currentFilter: SearchFilterIntent.SearchFilter): SearchFilterIntent
    fun createSearchableFragment(filter: SearchFilterIntent.SearchFilter): SearchContract.SearchableFragment<SearchFilterIntent.SearchFilter>
}