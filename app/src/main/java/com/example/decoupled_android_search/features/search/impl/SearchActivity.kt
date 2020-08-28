package com.example.decoupled_android_search.features.search.impl

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.contract.SearchFactory
import com.example.decoupled_android_search.features.search.contract.SearchFilterIntent
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchContract.SearchableActivity {
    companion object {
        private val PREFIX     = "${SearchActivity::class.qualifiedName}"

        val EXTRA_KEY          = "$PREFIX.extra"
        val SEARCH_FILTER_KEY  = "$PREFIX.searchFilter"
        val SEARCH_FACTORY_KEY = "$PREFIX.searchFactory"
    }

    private lateinit var searchFilter: SearchFilterIntent.SearchFilter
    private lateinit var searchFactory: SearchFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViews()

        if (activityIsRestarting(savedInstanceState)) {
            retrieveSavedInstanceState(savedInstanceState!!)
        } else {
            retrieveParametersFromIntent(intent)
            startSearchFilterActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val filter = extractFilterFromIntent(data)
            if (filter != null) {
                searchFilter = filter
                addFragment()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (searchFilter.isEmpty()) {
                finish()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putParcelable(SEARCH_FACTORY_KEY, searchFactory)
        outState.putParcelable(SEARCH_FILTER_KEY, searchFilter)

        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun showLoadingAnimation() {
        swipeRefresh.isRefreshing = true;
    }

    override fun hideLoadingAnimation() {
        swipeRefresh.isRefreshing = false;
    }

    override fun setAppBarTitle(title: String) {
        topAppBar.title = title;
    }

    override fun notifyInvalidFilter() {
        Toast.makeText(this, R.string.invalid_search_filter, Toast.LENGTH_LONG).show()
    }

    private fun initViews() {
        initAppBar()
        initFab()
        initSwipeRefresh()
    }

    private fun initAppBar() {
        topAppBar.title = ""
        setSupportActionBar(topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initFab() {
        fabFilter.setOnClickListener {
            startSearchFilterActivity()
        }
    }

    private fun initSwipeRefresh() {
        swipeRefresh.setOnRefreshListener {
            addFragment()
        }
    }

    private fun activityIsRestarting(savedInstanceState: Bundle?): Boolean {
        return savedInstanceState != null
    }

    private fun retrieveSavedInstanceState(savedInstanceState: Bundle) {
        val factory: SearchFactory? = retrieveSearchFactoryFromSavedState(savedInstanceState)
        val filter: SearchFilterIntent.SearchFilter? = retrieveSearchFilterFromSavedState(savedInstanceState)

        if (factory != null)
            this.searchFactory = factory

        if (filter != null)
            this.searchFilter = filter
    }

    private fun retrieveParametersFromIntent(intent: Intent) {
        val factory = retrieveSearchFactoryFromIntentExtras(intent)

        if (factory == null)
            finish()

        searchFactory = factory!!
        searchFilter  = factory.createEmptySearchFilter()
    }

    private fun retrieveSearchFilterFromSavedState(savedInstanceState: Bundle): SearchFilterIntent.SearchFilter? =
        savedInstanceState.getParcelable(SEARCH_FILTER_KEY)

    private fun retrieveSearchFactoryFromSavedState(savedInstanceState: Bundle): SearchFactory? =
        savedInstanceState.getParcelable(SEARCH_FACTORY_KEY)

    private fun retrieveSearchFactoryFromIntentExtras(intent: Intent): SearchFactory? =
        intent.getParcelableExtra(EXTRA_KEY)

    private fun startSearchFilterActivity() {
        val callingIntent = searchFactory.createSearchFilterIntent(this, searchFilter)

        startActivityForResult(callingIntent, 1)
    }

    private fun extractFilterFromIntent(intent: Intent?): SearchFilterIntent.SearchFilter? {
        val bundle = intent?.extras
        if (bundle != null)
            return SearchFilterIntent.SearchFilter.getFilterFrom(bundle)

        return null
    }

    private fun addFragment() {
        val fragment = searchFactory.createSearchableFragment(searchFilter)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}