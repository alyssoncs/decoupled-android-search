package com.example.decoupled_android_search.features.search.impl.dogs.ui.presenter

import com.example.decoupled_android_search.core.use_cases.dog_search.Breed
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchUseCase
import com.example.decoupled_android_search.core.use_cases.dog_search.SubBreed
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.view.DogSearchView
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.net.URL
import java.util.stream.Stream

fun <T> List<T>.clone() = ArrayList(this)

private val imageRepository = listOf(
    listOf(URL("https://www.google.com")),
    listOf(URL("https://www.fb.com")),
    listOf(URL("https://www.twitter.com")),
    listOf(URL("https://www.netflix.com")),
    listOf(URL("https://www.microsoft.com")),
    listOf(URL("https://www.youtube.com")),
)

@ExtendWith(MockitoExtension::class)
internal class DogSearchPresenterImplTest {
    @Mock
    private lateinit var view: DogSearchView

    @Mock
    private lateinit var useCase: DogSearchUseCase

    private lateinit var presenter: DogSearchPresenter

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun shouldFetchFirstPageOfBreedOnStart() {
        val filter = DogFilter(breed = "akita")
        initPresenter(filter)
        val breed = Breed("akita")
        given(useCase.getImages(breed, 0))
            .willReturn(imageRepository[0])

        presenter.onStart()

        assertUpdateImageList(imageRepository[0].clone())
    }

    @Test
    fun shouldFetchFirstPageOfSubBreedOnStart() {
        // given
        val filter = DogFilter(breed = "bulldog", subBreed = "french")
        initPresenter(filter)
        val subBreed = SubBreed(Breed("bulldog"), "french")
        given(useCase.getImages(subBreed, 0))
            .willReturn(imageRepository[0])

        // when
        presenter.onStart()

        // then
        assertUpdateImageList(imageRepository[0].clone())
    }

    @Test
    fun shouldShowLoadingAnimationBeforeFetchFirstPageOfBreedOnStart() {
        val filter = DogFilter(breed = "akita")
        initPresenter(filter)
        val breed = Breed("akita")
        given(useCase.getImages(breed, 0))
            .willReturn(imageRepository[0])

        presenter.onStart()

        assertShowLoadingAnimationBeforeUpdateImageList()
    }

    @Test
    fun shouldHideLoadingAnimationAfterFetchFirstPageOfBreedOnStart() {
        val filter = DogFilter(breed = "akita")
        initPresenter(filter)
        val breed = Breed("akita")
        given(useCase.getImages(breed, 0))
            .willReturn(imageRepository[0])

        presenter.onStart()

        assertHideLoadingAnimationBeforeUpdateImageList()
    }

    @Test
    fun shouldShowLoadingAnimationBeforeFetchFirstPageOfSubBreedOnStart() {
        val filter = DogFilter(breed = "bulldog", subBreed = "french")
        initPresenter(filter)
        val subBreed = SubBreed(Breed("bulldog"), "french")
        given(useCase.getImages(subBreed, 0))
            .willReturn(imageRepository[0])

        presenter.onStart()

        assertShowLoadingAnimationBeforeUpdateImageList()
    }

    @Test
    fun shouldHideLoadingAnimationAfterFetcFirstPageOfSubBreedOnStart() {
        val filter = DogFilter(breed = "bulldog", subBreed = "french")
        initPresenter(filter)
        val subBreed = SubBreed(Breed("bulldog"), "french")
        given(useCase.getImages(subBreed, 0))
            .willReturn(imageRepository[0])

        presenter.onStart()

        assertHideLoadingAnimationBeforeUpdateImageList()
    }

    @Test
    fun shouldNotifyUserOfInvalidFilterWhenTheFilterIsEmpty() {
        val filter = DogFilter.createEmpty()
        initPresenter(filter)

        presenter.onStart()

        assertNotifyInvalidSearchFilter()
    }

    @Test
    fun shouldNotNotifyUserOfInvalidFilterWhenTheFilterIsNotEmpty() {
        val filter = DogFilter(breed = "akita")
        initPresenter(filter)

        presenter.onStart()

        assertDontNotifyInvalidSearchFilter()
    }

    @Test
    fun shouldNotUpdateImagesWhenTheFilterIsEmpty() {
        val filter = DogFilter.createEmpty()
        initPresenter(filter)

        presenter.onStart()

        assertDontUpdateImageList()
    }

    @Test
    fun shouldDisplaySearchErrorMessageWhenBreedSearchThrows() {
        val filter = DogFilter("akita")
        initPresenter(filter)
        val breed = Breed("akita")
        given(useCase.getImages(breed, 0))
            .willAnswer {throw DogSearchUseCase.SearchException()}

        presenter.onStart()

        expectDisplaySearchErrorMessage()
    }

    @Test
    fun shouldDisplaySearchErrorMessageWhenSubBreedSearchThrows() {
        val filter = DogFilter(breed = "bulldog", subBreed = "french")
        initPresenter(filter)
        val subBreed = SubBreed(Breed("bulldog"), "french")
        given(useCase.getImages(subBreed, 0))
            .willAnswer {throw DogSearchUseCase.SearchException()}

        presenter.onStart()

        expectDisplaySearchErrorMessage()
    }

    @ParameterizedTest
    @ArgumentsSource(DogImagePagesProvider::class)
    fun shouldFetchNextPageWhenUserReachEndOfScroll(dogImagePages: List<List<URL>>) {
        // precondition
        assertThat(dogImagePages.size).isGreaterThan(1)

        // given
        val filter = DogFilter("akita")
        initPresenter(filter)
        for (page in dogImagePages.indices) {
            given(useCase.getImages(any<Breed>(), eq(page)))
                .willReturn(dogImagePages[page])
        }
        presenter.onStart()

        // when
        for (i in 1 until dogImagePages.size)
            presenter.onReachEndOfScroll()

        // then
        val inOrder = inOrder(view)
        for (i in 1 until dogImagePages.size)
            then(view)
                .should(inOrder)
                .updateImageList(dogImagePages[i].clone())
    }

    @Test
    fun shouldFetchPreviousPageWhenFetchingAgainAfterSearchFailure() {
        val filter = DogFilter("akita")
        initPresenter(filter)
        configureUseCaseToReturn(page = 0, returnValue = imageRepository[0])
        presenter.onReachEndOfScroll()
        configureUseCaseToFail(page = 1)
        presenter.onReachEndOfScroll()
        configureUseCaseToReturn(page = 1, returnValue = imageRepository[1])

        presenter.onReachEndOfScroll()

        then(view)
            .should()
            .updateImageList(imageRepository[1])
    }

    @Test
    fun shouldChangeAppBarTitleToDogBreedOnStart() {
        val filter = DogFilter("akita")
        initPresenter(filter)

        presenter.onStart()

        then(view)
            .should()
            .changeAppBarTitle("akita")
    }

    @Test
    fun shouldChangeAppBarTitleToDogBreedAndSubBreedOnStart() {
        val filter = DogFilter(breed = "bulldog", subBreed = "french")
        initPresenter(filter)

        presenter.onStart()

        then(view)
            .should()
            .changeAppBarTitle("french bulldog")
    }

    private fun initPresenter(filter: DogFilter) {
        presenter = DogSearchPresenterImpl(useCase, filter).apply {
            setView(view)
        }
    }

    private fun configureUseCaseToFail(page: Int) {
        given(useCase.getImages(any<Breed>(), eq(page)))
            .willAnswer { throw DogSearchUseCase.SearchException() }
    }

    private fun configureUseCaseToReturn(
        page: Int,
        returnValue: List<URL>
    ) {
        given(useCase.getImages(any<Breed>(), eq(page)))
            .willReturn(returnValue)
    }

    private fun assertUpdateImageList(imagesUrl: List<URL>) {
        then(view)
            .should()
            .updateImageList(imagesUrl)
    }

    private fun assertDontUpdateImageList() {
        then(view)
            .should(times(0))
            .updateImageList(any())
    }

    private fun assertShowLoadingAnimationBeforeUpdateImageList() {
        val inOrder = inOrder(view)
        then(view)
            .should(inOrder)
            .showLoadingAnimation()

        then(view)
            .should(inOrder)
            .updateImageList(any())
    }

    private fun assertHideLoadingAnimationBeforeUpdateImageList() {
        val inOrder = inOrder(view)
        then(view)
            .should(inOrder)
            .updateImageList(any())

        then(view)
            .should(inOrder)
            .hideLoadingAnimation()
    }

    private fun assertNotifyInvalidSearchFilter() {
        then(view)
            .should()
            .notifyInvalidSearchFilter()
    }

    private fun assertDontNotifyInvalidSearchFilter() {
        then(view)
            .should(times(0))
            .notifyInvalidSearchFilter()
    }

    private fun expectDisplaySearchErrorMessage() {
        then(view)
            .should()
            .displaySearchErrorMessage()
    }
}

class DogImagePagesProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(listOf(
                imageRepository[0],
                imageRepository[1],
            )),
            Arguments.of(listOf(
                imageRepository[0],
                imageRepository[1],
                imageRepository[2],
            )),
            Arguments.of(listOf(
                imageRepository[0],
                imageRepository[1],
                imageRepository[2],
                imageRepository[3],
            )),
        )
    }

}
