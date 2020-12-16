package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.decoupled_android_search.BuildConfig
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.RemotePaginatedDogRepositoryAdapter
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.DogsEndpoints
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchInteractor
import com.example.decoupled_android_search.features.search.contract.SearchFilter
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter.DogFilterPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter.DogFilterPresenterImpl
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.DogFilterView
import com.google.android.material.snackbar.Snackbar
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.activity_dog_filter.breedSpinner
import kotlinx.android.synthetic.main.activity_dog_filter.confirmButton
import kotlinx.android.synthetic.main.activity_dog_filter.loadingWidget
import kotlinx.android.synthetic.main.activity_dog_filter.rootContainer
import kotlinx.android.synthetic.main.activity_dog_filter.subBreedSpinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DogFilterActivity : AppCompatActivity() {
    private lateinit var viewModel: DogFilterViewModel
    private lateinit var presenter: DogFilterPresenter
    private lateinit var searchFilter: DogFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_filter)

        setUp()
        subscribeUi()

        if (savedInstanceState == null)
            presenter.onStart()

        confirmButton.setOnClickListener {
            presenter.onSubmitButtonClick()
        }
    }

    private fun setUp() {
        searchFilter = getSearchFilterFromIntent()
        viewModel = getViewModel()
        presenter = getPresenter(viewModel)
    }

    private fun getSearchFilterFromIntent(): DogFilter {
        val filter: SearchFilter? = intent.extras?.let {
            SearchFilter.getFilterFrom(it)
        }

        return if (filter != null)
            filter as DogFilter
        else
            DogFilter.createEmpty()
    }

    private fun getViewModel() = ViewModelProvider(this).get(DogFilterViewModel::class.java)

    private fun getPresenter(view: DogFilterView): DogFilterPresenter {
        val useCase = getUseCase()
        val presenter = DogFilterPresenterImpl(useCase)
        val viewModelFactory = DogFilterPresenterDispatcher.Factory(presenter)

        return ViewModelProvider(this, viewModelFactory)
            .get(DogFilterPresenterDispatcher::class.java).apply {
                setView(view)
                setFilter(searchFilter)
            }
    }

    private fun getUseCase(): DogSearchInteractor {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.DOG_API_BASE_URL}/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()
            )
            .build()
        val endpoint = retrofit.create(DogsEndpoints::class.java)
        val repository = RemotePaginatedDogRepositoryAdapter(endpoint)
        return DogSearchInteractor(repository)
    }

    private fun subscribeUi() {
        subscribeLoadingAnimation()
        subscribeBreedList()
        subscribeBreedSearchErrorMessage()
        subscribeSubBreedList()
        subscribeSubBreedSearchErrorMessage()
        subscribeReturnSelection()
        subscribeBreedIndexToSelect()
        subscribeSubBreedIndexToSelect()
    }

    private fun subscribeLoadingAnimation() {
        viewModel.shouldShowLoadingAnimation.observe(this) { showLoading ->
            if (showLoading)
                loadingWidget.visibility = View.VISIBLE
            else
                loadingWidget.visibility = View.INVISIBLE
        }
    }

    private fun subscribeBreedList() {
        viewModel.breedList.observe(this) { breeds ->
            ArrayAdapter(this, android.R.layout.simple_spinner_item, breeds).let {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                breedSpinner.apply {
                    adapter = it
                    onItemSelectedListener = breedSpinnerList
                }
            }
        }
    }

    private val breedSpinnerList by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                presenter.onBreedSelected(position)
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                presenter.onBreedDeselect()
            }
        }
    }

    private fun subscribeBreedSearchErrorMessage() {
        viewModel.notifyBreedSearchError.observe(this) { notifyEvent ->
            if (notifyEvent.getContentIfNotHandled() == true)
                Snackbar.make(
                    rootContainer,
                    "An error happened while fetching the breeds",
                    Snackbar.LENGTH_LONG
                ).show()
        }
    }

    private fun subscribeSubBreedList() {
        viewModel.subBreedList.observe(this) { subBreeds ->
            ArrayAdapter(this, android.R.layout.simple_spinner_item, subBreeds).let {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                subBreedSpinner.apply {
                    adapter = it
                    onItemSelectedListener = subBreedSpinnerList
                }
            }
        }
    }

    private val subBreedSpinnerList by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                presenter.onSubBreedSelected(position)
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                presenter.onSubBreedDeselected()
            }
        }
    }

    private fun subscribeSubBreedSearchErrorMessage() {
        viewModel.notifySubBreedSearchError.observe(this) { notifyEvent ->
            if (notifyEvent.getContentIfNotHandled() == true)
                Snackbar.make(
                    rootContainer,
                    "An error happened while fetching the sub breeds",
                    Snackbar.LENGTH_LONG
                ).show()
        }
    }

    private fun subscribeReturnSelection() {
        viewModel.returnSelection.observe(this) { returnEvent ->
            val event = returnEvent.getContentIfNotHandled()
            if (event?.shouldReturn == true) {
                val intent = Intent().apply {
                    val filter = event.dogFilter
                    putExtras(filter.toBundle())
                }

                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun subscribeBreedIndexToSelect() {
        viewModel.breedIndexToSelect.observe(this) { indexEvent ->
            indexEvent.getContentIfNotHandled()?.let {
                breedSpinner.selection = it
            }
        }
    }

    private fun subscribeSubBreedIndexToSelect() {
        viewModel.subBreedIndexToSelect.observe(this) { indexEvent ->
            indexEvent.getContentIfNotHandled()?.let {
                subBreedSpinner.selection = if (it >= 0) it else MaterialSpinner.INVALID_POSITION
            }
        }
    }
}

