package com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter

import com.example.decoupled_android_search.core.use_cases.dog_search.Breed
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchUseCase
import com.example.decoupled_android_search.core.use_cases.dog_search.SubBreed
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.view.DogFilterView
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.stream.Stream


@ExtendWith(MockitoExtension::class)
class DogFilterPresenterImplTest {
    @Mock
    private lateinit var useCase: DogSearchUseCase
    @Mock
    private lateinit var view: DogFilterView

    private lateinit var presenter: DogFilterPresenter

    @BeforeEach
    fun setUp() {
        presenter = DogFilterPresenterImpl(useCase)
        presenter.setView(view)
    }

    @Test
    fun shouldDisplayLoadingAnimationWhenStarting() {
        // given

        // when
        presenter.onStart()

        // then
        then(view)
            .should()
            .showLoadingAnimation()
    }

    @Test
    fun shouldHideLoadingAnimationAfterShowLoadingAnimation() {
        // given

        // when
        presenter.onStart()

        // then
        thenShouldShowThenHideLoadingAnimation()
    }

    @ParameterizedTest
    @ArgumentsSource(BreedsProvider::class)
    fun shouldSetBreedSelectionOptionsWhenStarting(
        availableBreeds: List<Breed>,
        optionsToSet: List<String>
    ) {
        given(useCase.getBreeds())
            .willReturn(availableBreeds)

        presenter.onStart()

        then(view)
            .should()
            .setBreedSelectionOptions(optionsToSet)
    }

    @Test
    fun shouldNotifyOfBreedSearchErrorWhenUseCaseThrows() {
        given(useCase.getBreeds())
            .willAnswer { throw DogSearchUseCase.SearchException() }

        presenter.onStart()

        then(view)
            .should()
            .notifyBreedSearchError()
    }

    @ParameterizedTest
    @ArgumentsSource(BreedAndSubBreedOptionsProvider::class)
    fun shouldSetSubBreedOptionsWhenUserSelectsBreed(
        breeds: List<Breed>,
        breedIndex: Int,
        subBreeds: List<SubBreed>,
        optionsToSet: List<String>
    ) {
        val selectedBreed = breeds[breedIndex]
        given(useCase.getBreeds())
            .willReturn(breeds)
        given(useCase.getSubBreeds(selectedBreed))
            .willReturn(subBreeds)
        presenter.onStart()

        presenter.onBreedSelected(breedIndex)

        then(view)
            .should()
            .setSubBreedSelectionOptions(optionsToSet)
    }

    @Test
    fun shouldNotifyOfSubBreedSearchErrorWhenUseCaseThrows() {
        given(useCase.getBreeds())
            .willReturn(listOf(Breed("bulldog")))
        given(useCase.getSubBreeds(any()))
            .willAnswer { throw DogSearchUseCase.SearchException() }
        presenter.onStart()

        presenter.onBreedSelected(0)

        then(view)
            .should()
            .notifySubBreedSearchError()
    }

    @Test
    fun shouldClearSubBreedSelectionWhenDeselectingBreed() {
        given(useCase.getBreeds())
            .willReturn(listOf(Breed("bulldog")))
        given(useCase.getSubBreeds(any()))
            .willReturn(
                listOf(
                    SubBreed(Breed("bulldog"), "french"),
                    SubBreed(Breed("bulldog"), "english"),
                )
            )
        presenter.onStart()
        presenter.onBreedSelected(0)

        presenter.onBreedDeselect()

        then(view)
            .should()
            .clearSubBreedOptions()
    }

    @Test
    fun shouldDisplayAndHideLoadingAnimationWhenSearchingSubBreeds() {
        given(useCase.getBreeds())
            .willReturn(listOf(Breed("bulldog")))
        given(useCase.getSubBreeds(any()))
            .willReturn(
                listOf(
                    SubBreed(Breed("bulldog"), "french"),
                    SubBreed(Breed("bulldog"), "english"),
                )
            )
        presenter.onStart()
        reset(view)

        presenter.onBreedSelected(0)

        thenShouldShowThenHideLoadingAnimation()
    }

    @Test
    fun shouldReturnFilterWithBreedOnlyOnSubmitButtonClick() {
        given(useCase.getBreeds())
            .willReturn(listOf(Breed("bulldog")))
        given(useCase.getSubBreeds(any()))
            .willReturn(
                listOf(
                    SubBreed(Breed("bulldog"), "french"),
                    SubBreed(Breed("bulldog"), "english"),
                )
            )
        presenter.onStart()
        presenter.onBreedSelected(0)

        presenter.onSubmitButtonClick()

        then(view)
            .should()
            .returnSearchFilter(argThat {filter ->
                filter.breed == "bulldog"
                        && filter.subBreed == ""
            })
    }

    @Test
    fun shouldReturnFilterWithBreedAndSubBreedOnSubmitButtonClick() {
        given(useCase.getBreeds())
            .willReturn(listOf(Breed("bulldog")))
        given(useCase.getSubBreeds(any()))
            .willReturn(
                listOf(
                    SubBreed(Breed("bulldog"), "french"),
                    SubBreed(Breed("bulldog"), "english"),
                )
            )
        presenter.onStart()
        presenter.onBreedSelected(0)
        presenter.onSubBreedSelected(1)

        presenter.onSubmitButtonClick()

        then(view)
            .should()
            .returnSearchFilter(argThat {filter ->
                filter.breed == "bulldog"
                        && filter.subBreed == "english"
            })
    }

    private fun thenShouldShowThenHideLoadingAnimation() {
        inOrder(view) {
            then(view)
                .should()
                .showLoadingAnimation()

            then(view)
                .should()
                .hideLoadingAnimation()
        }
    }
}

class BreedsProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                listOf(Breed("akita")),
                listOf("akita")
            ),
            Arguments.of(
                listOf(Breed("akita"), Breed("bulldog")),
                listOf("akita", "bulldog")
            )
        )
    }
}

class BreedAndSubBreedOptionsProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            argumentsOf(
                breeds = listOf(Breed("bulldog")),
                breedIndex = 0,
                subBreeds = listOf(
                    SubBreed(Breed("bulldog"), "french"),
                    SubBreed(Breed("bulldog"), "english"),
                ),
                optionsToSet = listOf( "french", "english" )
            ),
            argumentsOf(
                breeds = listOf(Breed("akita"), Breed("hound")),
                breedIndex = 1,
                subBreeds = listOf(
                    SubBreed(Breed("hound"), "afghan"),
                    SubBreed(Breed("hound"), "basset"),
                ),
                optionsToSet = listOf( "afghan", "basset" )
            ),
            argumentsOf(
                breeds = listOf(Breed("akita"), Breed("hound")),
                breedIndex = 0,
                subBreeds = emptyList(),
                optionsToSet = emptyList()
            ),
        )
    }

    private fun argumentsOf(
        breeds: List<Breed>,
        breedIndex: Int,
        subBreeds: List<SubBreed>,
        optionsToSet: List<String>
    ): Arguments {
        return Arguments.of(
            breeds,
            breedIndex,
            subBreeds,
            optionsToSet
        )
    }
}
