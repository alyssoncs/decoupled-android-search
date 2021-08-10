package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.concrete_infra.di.DaggerConcreteInfraComponent
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.impl.animes.di.AnimePresentationComponent
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter.AnimeSearchPresenter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_anime_search.animeList
import kotlinx.android.synthetic.main.fragment_anime_search.rootContainer
import javax.inject.Inject

class AnimeSearchFragment : SearchContract.SearchableFragment<AnimeFilter>() {

    @Inject
    lateinit var presenterDispatcherFactory: AnimeSearchPresenterDispatcher.Factory

    private val viewModel: AnimeSearchViewModel by lazy { createViewModel() }
    private val presenter: AnimeSearchPresenter by lazy { createPresenter(viewModel) }

    private val component: AnimePresentationComponent by lazy {
        DaggerConcreteInfraComponent.create()
            .animePresentationComponent()
    }

    override fun createSearchableFragment(): SearchContract.SearchableFragment<AnimeFilter> {
        return AnimeSearchFragment()
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_anime_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()

        if (savedInstanceState == null)
            presenter.onStart()
    }

    private fun createViewModel() = ViewModelProvider(this).get(AnimeSearchViewModel::class.java)

    private fun createPresenter(view: AnimeSearchView): AnimeSearchPresenter {
        return ViewModelProvider(this, presenterDispatcherFactory)
            .get(AnimeSearchPresenterDispatcher::class.java).apply {
                setView(view)
                setFilter(getSearchFilter() ?: AnimeFilter.createEmpty())
            }
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