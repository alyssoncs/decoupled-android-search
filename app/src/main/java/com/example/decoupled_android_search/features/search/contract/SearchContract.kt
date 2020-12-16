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
    }

    abstract class SearchableFragment<out Filter: SearchFilter>: Fragment() {
        companion object {
            private val SEARCH_FILTER_ARGUMENT_KEY =
                "${SearchableFragment::class.qualifiedName}.extra.search-filter"
        }

        protected lateinit var searchableActivity: SearchableActivity

        protected abstract fun createSearchableFragment(): SearchableFragment<Filter>

        fun newInstance(filter: SearchFilter): SearchableFragment<Filter> {
            return createSearchableFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SEARCH_FILTER_ARGUMENT_KEY, filter)
                }
            }
        }

        protected fun getSearchFilter(): Filter? {
            val nullableFilter: SearchFilter? = getFilter()

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

        private fun getFilter(): SearchFilter? {
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
        }
    }
}