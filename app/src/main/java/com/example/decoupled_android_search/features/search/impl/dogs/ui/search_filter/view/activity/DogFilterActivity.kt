package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.concrete_infra.paginated_dog_repository_stub.PaginatedDogRepositoryStub
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchInteractor
import com.example.decoupled_android_search.features.search.contract.SearchFilterIntent
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter.DogFilterPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter.DogFilterPresenterImpl
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.DogFilterView
import com.google.android.material.snackbar.Snackbar
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.activity_dog_filter.*
import kotlinx.android.synthetic.main.dog_search_fragment.*

class DogFilterActivity : AppCompatActivity() {
    private lateinit var viewModel: DogFilterViewModel
    private lateinit var presenter: DogFilterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_filter)

        viewModel = getViewModel()
        presenter = getPresenter(viewModel)

        subscribeUi()

        if (savedInstanceState == null)
            presenter.onStart()

        confirmButton.setOnClickListener {
            presenter.onSubmitButtonClick()
        }
    }

    private fun getViewModel() = ViewModelProvider(this).get(DogFilterViewModel::class.java)

    private fun getPresenter(view: DogFilterView): DogFilterPresenter {
        val repository = PaginatedDogRepositoryStub()
        val useCase = DogSearchInteractor(repository)
        val presenter = DogFilterPresenterImpl(useCase)
        val viewModelFactory = DogFilterPresenterDispatcher.Factory(presenter)

        return ViewModelProvider(this, viewModelFactory)
            .get(DogFilterPresenterDispatcher::class.java).apply {
                setView(view)
            }
    }

    private fun subscribeUi() {
        subscribeLoadingAnimation()
        subscribeBreedList()
        subscribeBreedSearchErrorMessage()
        subscribeSubBreedList()
        subscribeSubBreedSearchErrorMessage()
        subscribeReturnSelection()
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
        viewModel.returnSelection.observe(this) {returnEvent ->
            val event = returnEvent.getContentIfNotHandled()
            if (event?.shouldReturn == true) {
                val intent = SearchFilterIntent().apply {
                    val filter = event.dogFilter
                    putExtras(filter.toBundle())
                }

                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}

