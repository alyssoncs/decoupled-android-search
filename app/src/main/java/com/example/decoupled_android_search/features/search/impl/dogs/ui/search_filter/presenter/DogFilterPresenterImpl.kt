package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter

import com.example.decoupled_android_search.core.entities.Breed
import com.example.decoupled_android_search.core.entities.SubBreed
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchUseCase
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.DogFilterView

class DogFilterPresenterImpl(
    private val useCase: DogSearchUseCase
) : DogFilterPresenter {
    private lateinit var view: DogFilterView

    private var breeds: List<Breed> = emptyList()
    private var subBreeds: List<SubBreed> = emptyList()

    private var filter: DogFilter = DogFilter.createEmpty()

    override fun setFilter(filter: DogFilter) {
        this.filter = filter
    }

    override fun setView(view: DogFilterView) {
        this.view = view
    }

    override fun onStart() {
        wrapOnLoadingAnimation {
            try {
                tryToGetAndSetBreedOptions()
            } catch (e: DogSearchUseCase.SearchException) {
                view.notifyBreedSearchError()
            }
        }
    }

    override fun onBreedSelected(index: Int) {
        filter.breed = breeds[index].name

        wrapOnLoadingAnimation {
            try {
                tryToGetAndSetSubBreedOptions(index)
            } catch (e: DogSearchUseCase.SearchException) {
                view.notifySubBreedSearchError()
            }
        }
    }

    override fun onBreedDeselect() {
        filter.breed = ""
        view.clearSubBreedOptions()
    }

    override fun onSubBreedSelected(index: Int) {
        filter.subBreed = subBreeds[index].name
    }

    override fun onSubBreedDeselected() {
        filter.subBreed = ""
    }

    override fun onSubmitButtonClick() {
        view.returnSearchFilter(filter)
    }

    private fun wrapOnLoadingAnimation(doOperation: () -> Unit) {
        view.showLoadingAnimation()
        doOperation()
        view.hideLoadingAnimation()
    }

    private fun tryToGetAndSetBreedOptions() {
        breeds = useCase.getBreeds()
        val breedsName = fromBreedsToBreedsName(breeds)
        view.setBreedSelectionOptions(breedsName)

        if (breedsName.contains(filter.breed)) {
            val index = breedsName.indexOf(filter.breed)
            view.selectBreed(index)
            onBreedSelected(index)
        }
    }

    private fun fromBreedsToBreedsName(breeds: List<Breed>): List<String> {
        return breeds.map { it.name }
    }

    private fun tryToGetAndSetSubBreedOptions(index: Int) {
        subBreeds = useCase.getSubBreeds(breeds[index])
        val subBreedsName = fromSubBreedsToSubBreedsName(subBreeds)
        view.setSubBreedSelectionOptions(subBreedsName)

        if (subBreedsName.contains(filter.subBreed)) {
            view.selectSubBreed(subBreedsName.indexOf(filter.subBreed))
        } else {
            view.unselectSubBreed()
            onSubBreedDeselected()
        }
    }

    private fun fromSubBreedsToSubBreedsName(subBreeds: List<SubBreed>): List<String> {
        return subBreeds.map { it.name }
    }
}