package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.decoupled_android_search.BuildConfig
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.RemotePaginatedDogRepositoryAdapter
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.DogsEndpoints
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchInteractor
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.presenter.DogSearchPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.presenter.DogSearchPresenterImpl
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.DogSearchView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_dog_search.dogImageList
import kotlinx.android.synthetic.main.fragment_dog_search.rootContainer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DogSearchFragment: SearchContract.SearchableFragment<DogFilter>() {

    private lateinit var viewModel: DogSearchViewModel
    private lateinit var presenter: DogSearchPresenter

    override fun createSearchableFragment(): SearchContract.SearchableFragment<DogFilter> {
        return DogSearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dog_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel()
        presenter = getPresenter(viewModel)

        subscribeUi()

        if (savedInstanceState == null)
            presenter.onStart()
    }

    private fun getViewModel() = ViewModelProvider(this).get(DogSearchViewModel::class.java)

    private fun getPresenter(view: DogSearchView): DogSearchPresenter {
        val useCase = getUseCase()
        val presenter = DogSearchPresenterImpl(useCase, getSearchFilter() ?: DogFilter.createEmpty())
        presenter.setView(view)
        val viewModelFactory = DogSearchPresenterDispatcher.Factory(presenter)

        return ViewModelProvider(this, viewModelFactory)
            .get(DogSearchPresenterDispatcher::class.java)
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
        initRecyclerView()
        subscribeLoadingAnimation()
        subscribeInvalidSearchFilterMessage()
        subscribeSearchErrorMessage()
        subscribeImageList()
        subscribeAppBarTitle()
    }

    private var isLoading = false
    private fun initRecyclerView() {
        dogImageList.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = DogImagesAdapter(requireContext(), mutableListOf())
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0)
                        if (!isLoading)
                            if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN))
                                presenter.onReachEndOfScroll()
                }
            })
        }
    }

    private fun subscribeLoadingAnimation() {
        viewModel.shouldShowLoadingAnimation.observe(viewLifecycleOwner) { showLoading ->
            isLoading = showLoading

            if (showLoading)
                searchableActivity.showLoadingAnimation()
            else
                searchableActivity.hideLoadingAnimation()
        }
    }

    private fun subscribeInvalidSearchFilterMessage() {
        viewModel.shouldShowInvalidSearchFilter.observe(viewLifecycleOwner) { showMessageEvent ->
            if (showMessageEvent.getContentIfNotHandled() == true)
                searchableActivity.notifyInvalidFilter()
        }
    }

    private fun subscribeSearchErrorMessage() {
        viewModel.shouldShowSearchErrorMessage.observe(viewLifecycleOwner) { showMessageEvent ->
            if (showMessageEvent.getContentIfNotHandled() == true)
                Snackbar.make(
                    rootContainer,
                    "An error happened while fetching the images",
                    Snackbar.LENGTH_LONG
                ).show()
        }
    }

    private fun subscribeImageList() {
        viewModel.imageList.observe(viewLifecycleOwner) { urls ->
            val adapter = dogImageList.adapter as DogImagesAdapter
            adapter.updateList(urls)
        }
    }

    private fun subscribeAppBarTitle() {
        viewModel.appBarTitle.observe(viewLifecycleOwner) { title ->
            searchableActivity.setAppBarTitle(title)
        }
    }
}