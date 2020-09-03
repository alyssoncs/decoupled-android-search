package com.example.decoupled_android_search.features.search.contract

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

class SearchFilterIntent: Intent() {

    abstract class SearchFilter: Parcelable {
        companion object {
            private val EXTRA_SEARCH_FILTER_KEY =
                "${SearchFilterIntent::class.qualifiedName}.extra.search-filter"

            fun getFilterFrom(bundle: Bundle): SearchFilter? {
                return bundle.getParcelable(EXTRA_SEARCH_FILTER_KEY)
            }
        }

        abstract fun isEmpty(): Boolean


        fun toBundle(): Bundle {
            return Bundle().also {
                it.putParcelable(EXTRA_SEARCH_FILTER_KEY, this)
            }
        }
    }
}

