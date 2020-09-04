package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter

import com.example.decoupled_android_search.core.use_cases.dog_search.Breed
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchUseCase
import com.example.decoupled_android_search.core.use_cases.dog_search.SubBreed
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.DogFilterView

class DogFilterPresenterImpl(
    private val useCase: DogSearchUseCase
) : DogFilterPresenter {
    private lateinit var view: DogFilterView

    private var breeds: List<Breed> = emptyList()
    private var subBreeds: List<SubBreed> = emptyList()

    private var filter: DogFilter = DogFilter.createEmpty()

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
    }

    private fun fromBreedsToBreedsName(breeds: List<Breed>): List<String> {
        return breeds
            .asSequence()
            .map { it.name }
            .toList()
    }

    private fun tryToGetAndSetSubBreedOptions(index: Int) {
        subBreeds = useCase.getSubBreeds(breeds[index])
        val subBreedsName = fromSubBreedsToSubBreedsName(subBreeds)
        view.setSubBreedSelectionOptions(subBreedsName)
    }

    private fun fromSubBreedsToSubBreedsName(subBreeds: List<SubBreed>): List<String> {
        return subBreeds
            .asSequence()
            .map { it.name }
            .toList()
    }
}