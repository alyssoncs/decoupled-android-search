package com.example.decoupled_android_search.features.search.contract

import android.content.Context
import android.content.Intent
import android.os.Parcelable

interface SearchFactory: Parcelable {
    fun createEmptySearchFilter(): SearchFilter

    fun createSearchFilterIntent(
        context: Context,
        currentFilter: SearchFilter
    ): Intent

    fun createSearchableFragment(
        filter: SearchFilter
    ): SearchContract.SearchableFragment<SearchFilter>
}