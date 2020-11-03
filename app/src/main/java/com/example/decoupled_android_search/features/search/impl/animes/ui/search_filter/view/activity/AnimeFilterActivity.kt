package com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.concrete_infra.paginated_anime_repository_stub.PaginatedAnimeRepositoryStub
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchInteractor
import com.example.decoupled_android_search.features.search.contract.SearchFilterIntent
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.presenter.AnimeFilterPresenter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.presenter.AnimeFilterPresenterImpl
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.AnimeFilterView
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.activity_anime_filter.animeGenreSpinner
import kotlinx.android.synthetic.main.activity_anime_filter.animeNameInput
import kotlinx.android.synthetic.main.activity_anime_filter.animeRatingSpinner
import kotlinx.android.synthetic.main.activity_anime_filter.animeStatusSpinner
import kotlinx.android.synthetic.main.activity_anime_filter.confirmButton
import kotlinx.android.synthetic.main.activity_anime_filter.loadingWidget

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

class AnimeFilterActivity : AppCompatActivity() {

    private lateinit var viewModel: AnimeFilterViewModel
    private lateinit var presenter: AnimeFilterPresenter

    private lateinit var searchFilter: AnimeFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_filter)

        setUp()
        prepareListeners()
        observeViewModel()

        if (savedInstanceState == null)
            presenter.onStart(searchFilter)
    }

    private fun setUp() {
        searchFilter = getSearchFilterFromIntent()
        viewModel = getViewModel()
        presenter = getPresenter(viewModel)
    }

    private fun prepareListeners() {
        prepareAnimeNameListener()
        prepareConfirmButtonClickListener()
    }

    private fun observeViewModel() {
        observeLoadingAnimation()
        observeStatusList()
        observeRatings()
        observeRatingSearchError()
        observeGenres()
        observeGenresSearchError()
        observeStatusIndexToSelect()
        observeAnimeName()
        observeRatingIndexToSelect()
        observeGenreIndexToSelect()
        observeAnimeFilterToReturn()
    }

    private fun getSearchFilterFromIntent(): AnimeFilter {
        val filter: SearchFilterIntent.SearchFilter? = intent.extras?.let {
            SearchFilterIntent.SearchFilter.getFilterFrom(it)
        }

        return if (filter != null)
            filter as AnimeFilter
        else
            AnimeFilter.createEmpty()
    }

    private fun getViewModel() = ViewModelProvider(this).get(AnimeFilterViewModel::class.java)

    private fun getPresenter(view: AnimeFilterView): AnimeFilterPresenter {
        val repository = PaginatedAnimeRepositoryStub()
        val useCase = AnimeSearchInteractor(repository)
        val presenter = AnimeFilterPresenterImpl(useCase)
        val viewModelFactory = AnimeFilterPresenterDispatcher.Factory(presenter)

        return ViewModelProvider(this, viewModelFactory)
            .get(AnimeFilterPresenterDispatcher::class.java).apply {
                setView(view)
            }
    }

    private fun prepareAnimeNameListener() {
        animeNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s != null)
                    presenter.onAnimeNameChanged(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun prepareConfirmButtonClickListener() {
        confirmButton.setOnClickListener {
            presenter.onConfirmButtonClick()
        }
    }

    private fun observeLoadingAnimation() {
        viewModel.isLoading.observe(this) { isLoading ->
            loadingWidget.setVisible(isLoading)
        }
    }

    private fun observeStatusList() {
        viewModel.statusList.observe(this) { statusList ->
            configureSpinner(animeStatusSpinner, statusList, statusSpinnerListener)
        }
    }

    private val statusSpinnerListener by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                presenter.onStatusSelected(position)
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                presenter.onStatusUnselected()
            }
        }
    }

    private fun observeRatings() {
        viewModel.ratings.observe(this) { ratings ->
            configureSpinner(animeRatingSpinner, ratings, ratingsSpinnerListener)
        }
    }

    private val ratingsSpinnerListener by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                presenter.onRatingSelected(position)
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                presenter.onRatingUnselected()
            }
        }
    }

    private fun observeRatingSearchError() {
        viewModel.ratingsSearchHasFailed.observe(this) { itFailed ->
            if (itFailed)
                animeRatingSpinner.error = getString(R.string.anime_rating_search_error)
        }
    }

    private fun observeGenres() {
        viewModel.genres.observe(this) { ratings ->
            configureSpinner(animeRatingSpinner, ratings, genresSpinnerListener)
        }
    }

    private val genresSpinnerListener by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                presenter.onGenreSelected(position)
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                presenter.onGenreUnselected()
            }
        }
    }

    private fun observeGenresSearchError() {
        viewModel.genresSearchHasFailed.observe(this) { itFailed ->
            if (itFailed)
                animeGenreSpinner.error = getString(R.string.anime_genres_search_error)
        }
    }

    private fun observeStatusIndexToSelect() {
        viewModel.statusIndexToSelect.observe(this) { indexToSelectEvent ->
            indexToSelectEvent.getContentIfNotHandled()?.let {
                animeStatusSpinner.selection = it
            }
        }
    }

    private fun observeAnimeName() {
        viewModel.nameToDisplay.observe(this) { nameEvent ->
            nameEvent.getContentIfNotHandled()?.let {
                animeNameInput.setText(it)
            }
        }
    }

    private fun observeRatingIndexToSelect() {
        viewModel.ratingIndexToSelect.observe(this) { indexToSelectEvent ->
            indexToSelectEvent.getContentIfNotHandled()?.let {
                animeRatingSpinner.selection = it
            }
        }
    }

    private fun observeGenreIndexToSelect() {
        viewModel.genreIndexToSelect.observe(this) { indexToSelectEvent ->
            indexToSelectEvent.getContentIfNotHandled()?.let {
                animeGenreSpinner.selection = it
            }
        }
    }

    private fun observeAnimeFilterToReturn() {
        viewModel.animeFilterToReturn.observe(this) { animeFilterEvent ->
            animeFilterEvent.getContentIfNotHandled()?.let {
                val intent = SearchFilterIntent().apply {
                    putExtras(it.toBundle())
                }

                setResult(RESULT_OK, intent)
            }
        }
    }

    private fun configureSpinner(spinner: MaterialSpinner, items: List<String>, listener: MaterialSpinner.OnItemSelectedListener) {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, items).let {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.apply {
                adapter = it
                onItemSelectedListener = listener
            }
        }
    }
}