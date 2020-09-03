package com.example.decoupled_android_search.features.search.impl.dogs.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.decoupled_android_search.R
import com.example.decoupled_android_search.concrete_infra.paginated_dog_repository_stub.PaginatedDogRepositoryStub
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchInteractor
import com.example.decoupled_android_search.features.search.contract.SearchContract
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.presenter.DogSearchPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.presenter.DogSearchPresenterImpl
import com.example.decoupled_android_search.features.search.impl.dogs.ui.view.DogSearchView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dog_search_fragment.*

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
        return inflater.inflate(R.layout.dog_search_fragment, container, false)
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
        val repository = PaginatedDogRepositoryStub()
        val useCase = DogSearchInteractor(repository)
        val presenter = DogSearchPresenterImpl(useCase, getSearchFilter() ?: DogFilter.createEmpty())
        presenter.setView(view)
        val viewModelFactory = DogSearchPresenterDispatcher.Factory(presenter)

        return ViewModelProvider(this, viewModelFactory)
            .get(DogSearchPresenterDispatcher::class.java)
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