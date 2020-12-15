package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.decoupled_android_search.BuildConfig
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.AnimeEndpoints
import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.RemotePaginatedAnimeRepositoryAdapter
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchInteractor
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter.AnimeSearchPresenter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter.AnimeSearchPresenterImpl
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_anime_search.animeList
import kotlinx.android.synthetic.main.fragment_anime_search.rootContainer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AnimeSearchFragment : SearchContract.SearchableFragment<AnimeFilter>() {

    private lateinit var viewModel: AnimeSearchViewModel
    private lateinit var presenter: AnimeSearchPresenter
    override fun createSearchableFragment(): SearchContract.SearchableFragment<AnimeFilter> {
        return AnimeSearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_anime_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel()
        presenter = getPresenter(viewModel)

        subscribeUi()

        if (savedInstanceState == null)
            presenter.onStart()
    }

    private fun getViewModel() = ViewModelProvider(this).get(AnimeSearchViewModel::class.java)

    private fun getPresenter(view: AnimeSearchView): AnimeSearchPresenter {
        val useCase = getUseCase()
        val presenter = AnimeSearchPresenterImpl(useCase).apply {
            setView(view)
            setFilter(getSearchFilter() ?: AnimeFilter.createEmpty())
        }
        val viewModelFactory = AnimeSearchPresenterDispatcher.Factory(presenter)
        return ViewModelProvider(this, viewModelFactory)
            .get(AnimeSearchPresenterDispatcher::class.java)
    }

    private fun getUseCase(): AnimeSearchUseCase {
        val retrofit = Retrofit.Builder()
            .baseUrl("${BuildConfig.JIKAN_BASE_URL}/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()
            )
            .build()
        val endpoint = retrofit.create(AnimeEndpoints::class.java)
        val repository = RemotePaginatedAnimeRepositoryAdapter(endpoint)
        return AnimeSearchInteractor(repository)
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
        animeList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AnimeAdapter(requireContext(), mutableListOf())
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
        viewModel.animeList.observe(viewLifecycleOwner) { urls ->
            val adapter = animeList.adapter as AnimeAdapter
            adapter.updateList(urls)
        }
    }

    private fun subscribeAppBarTitle() {
        viewModel.appBarTitle.observe(viewLifecycleOwner) { title ->
            searchableActivity.setAppBarTitle(title)
        }
    }

}