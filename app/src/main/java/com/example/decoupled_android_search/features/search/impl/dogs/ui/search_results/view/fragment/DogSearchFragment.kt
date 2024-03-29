package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.concrete_infra.di.DaggerConcreteInfraComponent
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.impl.dogs.di.DogPresentationComponent
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.presenter.DogSearchPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.DogSearchView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_dog_search.dogImageList
import kotlinx.android.synthetic.main.fragment_dog_search.rootContainer
import javax.inject.Inject

class DogSearchFragment: SearchContract.SearchableFragment<DogFilter>() {

    @Inject
    lateinit var presenterDispatcherFactory: DogSearchPresenterDispatcher.Factory

    private val viewModel: DogSearchViewModel by lazy { createViewModel() }
    private val presenter: DogSearchPresenter by lazy { createPresenter(viewModel) }

    private val component: DogPresentationComponent by lazy {
        val filter = getSearchFilter() ?: DogFilter.createEmpty()
        DaggerConcreteInfraComponent.create()
            .dogPresentationComponentBuilder()
            .addDogFilter(filter)
            .build()
    }

    override fun createSearchableFragment(): SearchContract.SearchableFragment<DogFilter> {
        return DogSearchFragment()
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dog_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()

        if (savedInstanceState == null)
            presenter.onStart()
    }

    private fun createViewModel() = ViewModelProvider(this).get(DogSearchViewModel::class.java)

    private fun createPresenter(view: DogSearchView): DogSearchPresenter {
        return ViewModelProvider(this, presenterDispatcherFactory)
            .get(DogSearchPresenterDispatcher::class.java).apply {
                setView(view)
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