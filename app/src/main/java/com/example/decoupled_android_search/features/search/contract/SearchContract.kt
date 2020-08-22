package com.example.decoupled_android_search.features.search.contract

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment

interface SearchContract {
    interface SearchableActivity {
        fun showLoadingAnimation()
        fun hideLoadingAnimation()
        fun setAppBarTitle(title: String)
        fun notifyInvalidFilter()
        fun notifyEmptyFilter()
    }

    abstract class SearchableFragment<out Filter: SearchFilterIntent.SearchFilter>: Fragment() {
        companion object {
            private val SEARCH_FILTER_ARGUMENT_KEY =
                "${SearchableFragment::class.qualifiedName}.extra.search-filter"
        }

        lateinit var searchableActivity: SearchableActivity

        abstract fun createSearchableFragment(): SearchableFragment<Filter>

        fun newInstance(filter: SearchFilterIntent.SearchFilter): SearchableFragment<Filter> {
            return createSearchableFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SEARCH_FILTER_ARGUMENT_KEY, filter)
                }
            }
        }

        fun getSearchFilter(): Filter? {
            val nullableFilter: SearchFilterIntent.SearchFilter? = getFilter()

            return if (nullableFilter == null) {
                null
            } else {
                try {
                    @Suppress("UNCHECKED_CAST")
                    nullableFilter as Filter
                } catch (e: ClassCastException) {
                    null
                }
            }
        }

        private fun getFilter(): SearchFilterIntent.SearchFilter? {
            return arguments?.getParcelable(SEARCH_FILTER_ARGUMENT_KEY)
        }

        @CallSuper
        override fun onAttach(context: Context) {
            super.onAttach(context)

            if (context is SearchableActivity) {
                searchableActivity = context
            } else {
                throw UnsupportedOperationException("This fragment must be attached by a " +
                        "${SearchableActivity::class.java.name} instance")
            }
        }

        @CallSuper
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val nullableFilter = getSearchFilter()

            if (nullableFilter == null)
                searchableActivity.notifyInvalidFilter()
        }

        @CallSuper
        override fun onDetach() {
            super.onDetach()
            searchableActivity = emptySearchableActivity
        }

        private val emptySearchableActivity = object: SearchableActivity {
            override fun showLoadingAnimation() { }

            override fun hideLoadingAnimation() { }

            override fun setAppBarTitle(title: String) { }

            override fun notifyInvalidFilter() { }

            override fun notifyEmptyFilter() { }
        }
    }
}