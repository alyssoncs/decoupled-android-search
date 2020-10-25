package com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.presenter

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.AnimeFilterView
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.then
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import java.util.stream.Stream


@DisplayName("AnimeFilterPresenterImpl tests")
@ExtendWith(MockitoExtension::class)
class AnimeFilterPresenterImplTest {

    @Mock
    private lateinit var useCase: AnimeSearchUseCase

    @Mock
    private lateinit var view: AnimeFilterView

    private lateinit var presenter: AnimeFilterPresenter

    @BeforeEach
    fun setUp() {
        presenter = AnimeFilterPresenterImpl(useCase).apply {
            setView(view)
        }
    }

    @DisplayName("onStart()")
    @Nested
    inner class OnStartTest {

        @Test
        fun `on start, then should show loading animation`() {
            presenter.onStart()

            then(view)
                .should()
                .showLoadingAnimation()
        }

        @Test
        fun `on start, then should hide loading animation after showing loading animation`() {
            presenter.onStart()

            inOrder(view) {
                then(view)
                    .should(this)
                    .showLoadingAnimation()
                then(view)
                    .should(this)
                    .hideLoadingAnimation()
            }
        }

        @ParameterizedTest
        @ArgumentsSource(StatusProvider::class)
        fun `given use case returns status list, on start, then should update status list`(expectedStatusList: List<AnimeQuery.Status> ) {
            given(useCase.getStatus())
                .willReturn(expectedStatusList)

            presenter.onStart()

            thenUpdateStatusList(expectedStatusList)
        }

        @ParameterizedTest
        @ArgumentsSource(RatingsProvider::class)
        fun `given use case returns ratings, on start, then should update ratings list`(
            givenRatings: List<AnimeQuery.Rating>,
            expectedRatingsDescriptions: List<String>
        ) {
            given(useCase.getRatings())
                .willReturn(givenRatings)

            presenter.onStart()

            thenUpdateRatingsList(expectedRatingsDescriptions)
        }

        @Test
        fun `given use case fails to fetch ratings, on start, then should notify ratings search failure`() {
            given(useCase.getRatings())
                .willAnswer { throw AnimeSearchUseCase.SearchException() }

            presenter.onStart()

            thenNotifyRatingsSearchFailure()
        }

        @ParameterizedTest
        @ArgumentsSource(GenresProvider::class)
        fun `given use case returns genres, on start, then should update genre list`(
            givenGenres: List<AnimeQuery.Genre>,
            expectedGenresDescriptions: List<String>
        ) {
            given(useCase.getGenres())
                .willReturn(givenGenres)

            presenter.onStart()

            thenUpdateGenreList(expectedGenresDescriptions)
        }

        @Test
        fun `given use case fails to fetch genres, on start, then should notify genres search failure`() {
            given(useCase.getGenres())
                .willAnswer { throw AnimeSearchUseCase.SearchException() }

            presenter.onStart()

            thenNotifyGenresSearchFailure()
        }

        @Test
        fun `given empty anime search filter, on start, then don't display any search name`() {
            val emptyFilter = AnimeFilter.createEmpty()

            presenter.onStart(emptyFilter)

            then(view)
                .should(never())
                .displaySearchName(any())
        }

        @ParameterizedTest
        @ArgumentsSource(AnimeFilterWithNameProvider::class)
        fun `given anime search filter with name , on start, then display search name`(
            filter: AnimeFilter,
            animeName: String
        ) {
            presenter.onStart(filter)

            then(view)
                .should()
                .displaySearchName(animeName)
        }

        @Test
        fun `given anime search filter with no status, on start, then don't select any status`() {
            val filter = AnimeFilter.createEmpty()

            presenter.onStart(filter)

            then(view)
                .should(never())
                .selectStatus(any())
        }

        @Test
        fun `given anime search filter with status and use case fails to fetch status, on start, then don't select any status`() {
            val filter = AnimeFilter(
                status = AnimeQuery.Status.AIRING
            )
            given(useCase.getGenres())
                .willAnswer { throw AnimeSearchUseCase.SearchException() }

            presenter.onStart(filter)

            then(view)
                .should(never())
                .selectStatus(any())
        }

        @Test
        fun `given anime search filter with status not present on use case response, on start, then don't select any status`() {
            val filterWithStatusNotPresentOnUseCaseResponse = AnimeFilter(
                status = AnimeQuery.Status.AIRING
            )
            given(useCase.getStatus())
                .willReturn(
                    listOf(
                        AnimeQuery.Status.TO_BE_AIRED,
                        AnimeQuery.Status.COMPLETED,
                    )
                )

            presenter.onStart(filterWithStatusNotPresentOnUseCaseResponse)

            then(view)
                .should(never())
                .selectStatus(any())
        }

        @ParameterizedTest
        @ArgumentsSource(AnimeStatusPresentOnUseCaseArgumentTestProvider::class)
        fun `given anime search filter with status present on use case response, on start, then select corresponding status`(
            filter: AnimeFilter,
            useCaseResponse: List<AnimeQuery.Status>,
            statusIndex: Int
        ) {
            given(useCase.getStatus())
                .willReturn(useCaseResponse)

            presenter.onStart(filter)

            then(view)
                .should()
                .selectStatus(statusIndex)
        }

        @Test
        fun `given anime search filter with no rating, on start, then don't select any rating`() {
            val filter = AnimeFilter.createEmpty()

            presenter.onStart(filter)

            then(view)
                .should(never())
                .selectRating(any())
        }

        @Test
        fun `given anime search filter with rating and use case fails to fetch rating, on start, then don't select any rating`() {
            val filter = AnimeFilter(
                rating = AnimeFilter.Rating(
                    rating = "violence",
                    ratingId = "+17"
                )
            )
            given(useCase.getRatings())
                .willAnswer { throw AnimeSearchUseCase.SearchException() }

            presenter.onStart(filter)

            then(view)
                .should(never())
                .selectRating(any())
        }

        @Test
        fun `given anime search filter with rating not present on use case response, on start, then don't select any rating`() {
            val sameName = "violence"
            val filterWithRatingNotPresentOnUseCaseResponse = AnimeFilter(
                rating = AnimeFilter.Rating(
                    rating = sameName,
                    ratingId = "+17"
                )
            )
            given(useCase.getRatings())
                .willReturn(
                    listOf(
                        AnimeQuery.Rating(
                            name = sameName,
                            id = "+18"
                        )
                    )
                )

            presenter.onStart(filterWithRatingNotPresentOnUseCaseResponse)

            then(view)
                .should(never())
                .selectRating(any())
        }


        @ParameterizedTest
        @ArgumentsSource(AnimeRatingPresentOnUseCaseArgumentTestProvider::class)
        fun `given anime search filter with rating present on use case response, on start, then select corresponding rating`(
            filter: AnimeFilter,
            useCaseResponse: List<AnimeQuery.Rating>,
            ratingIndex: Int
        ) {
            given(useCase.getRatings())
                .willReturn(useCaseResponse)

            presenter.onStart(filter)

            then(view)
                .should()
                .selectRating(ratingIndex)
        }

        @Test
        fun `given anime search filter with no genre, on start, then don't select any genre`() {
            val filter = AnimeFilter.createEmpty()

            presenter.onStart(filter)

            then(view)
                .should(never())
                .selectGenre(any())
        }

        @Test
        fun `given anime search filter with genre and use case fails to fetch genre, on start, then don't select any genre`() {
            val filter = AnimeFilter(
                genre = AnimeFilter.Genre(
                    genre = "action",
                    genreId = "act"
                ),
            )
            given(useCase.getGenres())
                .willAnswer { throw AnimeSearchUseCase.SearchException() }

            presenter.onStart(filter)

            then(view)
                .should(never())
                .selectGenre(any())
        }

        @Test
        fun `given anime search filter with genre not present on use case response, on start, then don't select any genre`() {
            val sameName = "action"
            val filterWithGenreNotPresentOnUseCaseResponse = AnimeFilter(
                genre = AnimeFilter.Genre(
                    genre = sameName,
                    genreId = "act"
                ),
            )
            given(useCase.getGenres())
                .willReturn(
                    listOf(
                        AnimeQuery.Genre(
                            name = sameName,
                            id = "roman"
                        )
                    )
                )

            presenter.onStart(filterWithGenreNotPresentOnUseCaseResponse)

            then(view)
                .should(never())
                .selectGenre(any())
        }

        @ParameterizedTest
        @ArgumentsSource(AnimeGenrePresentOnUseCaseArgumentTestProvider::class)
        fun `given anime search filter with genre present on use case response, on start, then select corresponding genre`(
            filter: AnimeFilter,
            useCaseResponse: List<AnimeQuery.Genre>,
            ratingIndex: Int
        ) {
            given(useCase.getGenres())
                .willReturn(useCaseResponse)

            presenter.onStart(filter)

            then(view)
                .should()
                .selectGenre(ratingIndex)
        }

        private fun thenUpdateStatusList(expectedStatusList: List<AnimeQuery.Status>) {
            thenDoOperationInsideLoadingAnimation {
                then(view)
                    .should(this)
                    .updateStatusList(expectedStatusList)
            }
        }

        private fun thenUpdateRatingsList(expectedRatings: List<String>) {
            thenDoOperationInsideLoadingAnimation {
                then(view)
                    .should(this)
                    .updateRatingsList(expectedRatings)
            }
        }

        private fun thenNotifyRatingsSearchFailure() {
            thenDoOperationInsideLoadingAnimation {
                then(view)
                    .should(this)
                    .notifyRatingsSearchFailure()
            }
        }

        private fun thenUpdateGenreList(expectedGenres: List<String>) {
            thenDoOperationInsideLoadingAnimation {
                then(view)
                    .should(this)
                    .updateGenreList(expectedGenres)
            }
        }

        private fun thenNotifyGenresSearchFailure() {
            thenDoOperationInsideLoadingAnimation {
                then(view)
                    .should(this)
                    .notifyGenresSearchFailure()
            }
        }

        private fun thenDoOperationInsideLoadingAnimation(doOperation: InOrder.() -> Unit) {
            inOrder(view) {
                then(view)
                    .should(this)
                    .showLoadingAnimation()

                doOperation()

                then(view)
                    .should(this)
                    .hideLoadingAnimation()
            }
        }
    }

    @DisplayName("onConfirmButtonClick()")
    @Nested
    inner class OnConfirmButtonClick {

        private val statusList = listOf(
            AnimeQuery.Status.AIRING,
            AnimeQuery.Status.TO_BE_AIRED,
            AnimeQuery.Status.COMPLETED,
        )

        private val ratingList = listOf(
            AnimeQuery.Rating(name = "all ages", id ="all"),
            AnimeQuery.Rating(name = "children", id ="child"),
            AnimeQuery.Rating(name = "teens", id ="+13"),
            AnimeQuery.Rating(name = "violence", id ="+17"),
            AnimeQuery.Rating(name = "sex", id ="+18"),
        )

        private val genreList = listOf(
            AnimeQuery.Genre(name = "action", id ="act"),
            AnimeQuery.Genre(name = "adventure", id ="adv"),
            AnimeQuery.Genre(name = "comedy", id ="come"),
            AnimeQuery.Genre(name = "mystery", id ="mys"),
            AnimeQuery.Genre(name = "drama", id ="drama"),
        )

        @BeforeEach
        fun setUp() {
            useCase.apply {
                lenient()
                    .`when`(getStatus())
                    .thenReturn(statusList)

                lenient()
                    .`when`(getRatings())
                    .thenReturn(ratingList)

                lenient()
                    .`when`(getGenres())
                    .thenReturn(genreList)
            }

            presenter.onStart()
        }

        @TestFactory
        fun `given anime name entered, on confirm button click, then return corresponding filter`() =
            listOf("naruto", "death note", "one piece")
                .map { it to AnimeFilter(name = it) }
                .map { (animeName, expectedFilter) ->
                    dynamicTest("given \"$animeName\" as the anime name entered, on confirm button click, then return filter with name \"$animeName\"") {

                        presenter.onAnimeNameChanged(animeName)

                        presenter.onConfirmButtonClick()

                        then(view)
                            .should()
                            .returnFilter(expectedFilter)
                    }
                }

        @DisplayName("when something is selected")
        @Nested
        inner class WhenSomethingIsSelected {

            @TestFactory
            fun `given status selected, on confirm button click, then return corresponding filter`() =
                statusList.indices
                    .map { it to AnimeFilter(status = statusList[it]) }
                    .map { (statusIndex, expectedFilter) ->
                        dynamicTest("given status with index $statusIndex selected, on confirm button click, then return filter with status ${expectedFilter.status}") {
                            presenter.onStatusSelected(statusIndex)

                            presenter.onConfirmButtonClick()

                            then(view)
                                .should()
                                .returnFilter(expectedFilter)
                        }
                    }

            @TestFactory
            fun `given rating selected, on confirm button click, then return corresponding filter`() =
                ratingList.indices
                    .map { it to ratingList[it] }
                    .map { (ratingIndex, useCaseRating) -> ratingIndex to AnimeFilter.Rating(useCaseRating) }
                    .map { (ratingIndex, filterRating) -> ratingIndex to AnimeFilter(rating = filterRating) }
                    .map { (ratingIndex, expectedFilter) ->
                        dynamicTest("given rating with index $ratingIndex selected, on confirm button click, then return filter with rating \"${expectedFilter.rating?.rating}\"") {
                            presenter.onRatingSelected(ratingIndex)

                            presenter.onConfirmButtonClick()

                            then(view)
                                .should()
                                .returnFilter(expectedFilter)
                        }
                    }

            @TestFactory
            fun `given genre selected, on confirm button click, then return corresponding filter`() =
                ratingList.indices
                    .map { it to genreList[it] }
                    .map { (genreIndex, useCaseGenre) -> genreIndex to AnimeFilter.Genre(useCaseGenre) }
                    .map { (genreIndex, filterGenre) -> genreIndex to AnimeFilter(genre = filterGenre) }
                    .map { (genreIndex, expectedFilter) ->
                        dynamicTest("given genre with index $genreIndex selected, on confirm button click, then return filter with genre \"${expectedFilter.genre?.genre}\"") {
                            presenter.onGenreSelected(genreIndex)

                            presenter.onConfirmButtonClick()

                            then(view)
                                .should()
                                .returnFilter(expectedFilter)
                        }
                    }
        }

        @DisplayName("when something is unselected")
        @Nested
        inner class WhenSomethingIsUnselected {

            @BeforeEach
            fun setUp() {
                presenter.apply {
                    onStatusSelected(0)
                    onRatingSelected(0)
                    onGenreSelected(0)
                }
            }

            @Test
            fun `given status unselected, on confirm button click, then return corresponding filter`() {
                presenter.onStatusUnselected()

                presenter.onConfirmButtonClick()

                argumentCaptor<AnimeFilter>().apply {
                    then(view)
                        .should()
                        .returnFilter(capture())

                    assertThat(firstValue.status).isNull()
                }
            }

            @Test
            fun `given rating unselected, on confirm button click, then return corresponding filter`() {
                presenter.onRatingUnselected()

                presenter.onConfirmButtonClick()

                argumentCaptor<AnimeFilter>().apply {
                    then(view)
                        .should()
                        .returnFilter(capture())

                    assertThat(firstValue.rating).isNull()
                }
            }

            @Test
            fun `given genre unselected, on confirm button click, then return corresponding filter`() {
                presenter.onGenreUnselected()

                presenter.onConfirmButtonClick()

                argumentCaptor<AnimeFilter>().apply {
                    then(view)
                        .should()
                        .returnFilter(capture())

                    assertThat(firstValue.genre).isNull()
                }
            }
        }

        @DisplayName("when something out of bounds is selected")
        @Nested
        inner class WhenSomethingOutOfBoundsIsSelected {

            private lateinit var previousFilter: AnimeFilter

            @BeforeEach
            internal fun setUp() {
                presenter.onConfirmButtonClick()

                argumentCaptor<AnimeFilter>().apply {
                    then(view)
                        .should()
                        .returnFilter(capture())

                    previousFilter = firstValue
                }

                clearInvocations(view)
            }

            @Test
            fun `given out of bounds status selected, on confirm button click, then return unaltered filter`() {
                val outOfBoundsIndex = statusList.size
                presenter.onStatusSelected(outOfBoundsIndex)

                presenter.onConfirmButtonClick()

                then(view)
                    .should()
                    .returnFilter(previousFilter)
            }

            @Test
            fun `given out of bounds rating selected, on confirm button click, then return unaltered filter`() {
                val outOfBoundsIndex = ratingList.size
                presenter.onRatingSelected(outOfBoundsIndex)

                presenter.onConfirmButtonClick()

                then(view)
                    .should()
                    .returnFilter(previousFilter)
            }

            @Test
            fun `given out of bounds genre selected, on confirm button click, then return unaltered filter`() {
                val outOfBoundsIndex = genreList.size
                presenter.onGenreSelected(outOfBoundsIndex)

                presenter.onConfirmButtonClick()

                then(view)
                    .should()
                    .returnFilter(previousFilter)
            }
        }
    }
}

class StatusProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                listOf(
                    AnimeQuery.Status.AIRING,
                )
            ),
            Arguments.of(
                listOf(
                    AnimeQuery.Status.AIRING,
                    AnimeQuery.Status.COMPLETED,
                )
            ),
            Arguments.of(
                listOf(
                    AnimeQuery.Status.AIRING,
                    AnimeQuery.Status.COMPLETED,
                    AnimeQuery.Status.TO_BE_AIRED,
                )
            ),
        )
    }
}

class RatingsProvider: ArgumentsProvider{
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                listOf(
                    AnimeQuery.Rating(name = "violence", id = "+17"),
                ),
                listOf(
                    "violence",
                ),
            ),
            Arguments.of(
                listOf(
                    AnimeQuery.Rating(name = "violence", id = "+17"),
                    AnimeQuery.Rating(name = "nudity", id = "+18"),
                ),
                listOf(
                    "violence",
                    "nudity"
                ),
            ),
        )
    }
}

class GenresProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                listOf(
                    AnimeQuery.Genre(name = "romance", id = "romance"),
                ),
                listOf(
                    "romance",
                )
            ),
            Arguments.of(
                listOf(
                    AnimeQuery.Genre(name = "romance", id = "romance"),
                    AnimeQuery.Genre(name = "action", id = "act"),
                ),
                listOf(
                    "romance",
                    "action",
                )
            ),
        )
    }
}

class AnimeFilterWithNameProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                AnimeFilter(name = "naruto"),
                "naruto",
            ),
            Arguments.of(
                AnimeFilter(name = "one piece"),
                "one piece"
            ),
        )
    }
}

class AnimeStatusPresentOnUseCaseArgumentTestProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                AnimeFilter(status = AnimeQuery.Status.COMPLETED),
                listOf(AnimeQuery.Status.COMPLETED),
                0,
            ),
            Arguments.of(
                AnimeFilter(status = AnimeQuery.Status.COMPLETED),
                listOf(AnimeQuery.Status.TO_BE_AIRED, AnimeQuery.Status.COMPLETED),
                1,
            ),
            Arguments.of(
                AnimeFilter(status = AnimeQuery.Status.COMPLETED),
                listOf(AnimeQuery.Status.AIRING, AnimeQuery.Status.TO_BE_AIRED, AnimeQuery.Status.COMPLETED),
                2,
            ),
        )
    }
}

class AnimeRatingPresentOnUseCaseArgumentTestProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                AnimeFilter(
                    rating = AnimeFilter.Rating(rating = "violence", ratingId = "+17")
                ),
                listOf(
                    AnimeQuery.Rating(name = "irrelevant name", id = "+17"),
                ),
                0,
            ),
            Arguments.of(
                AnimeFilter(
                    rating = AnimeFilter.Rating(rating = "violence", ratingId = "+17")
                ),
                listOf(
                    AnimeQuery.Rating(name = "irrelevant name", id = "+18"),
                    AnimeQuery.Rating(name = "irrelevant name", id = "+17"),
                ),
                1,
            ),
            Arguments.of(
                AnimeFilter(
                    rating = AnimeFilter.Rating(rating = "violence", ratingId = "+17")
                ),
                listOf(
                    AnimeQuery.Rating(name = "irrelevant name", id = "+18"),
                    AnimeQuery.Rating(name = "irrelevant name", id = "action"),
                    AnimeQuery.Rating(name = "irrelevant name", id = "+17"),
                ),
                2,
            ),
        )
    }
}

class AnimeGenrePresentOnUseCaseArgumentTestProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                AnimeFilter(
                    genre = AnimeFilter.Genre(genre = "action", genreId = "act")
                ),
                listOf(
                    AnimeQuery.Genre(name = "irrelevant name", id = "act"),
                ),
                0,
            ),
            Arguments.of(
                AnimeFilter(
                    genre = AnimeFilter.Genre(genre = "action", genreId = "act")
                ),
                listOf(
                    AnimeQuery.Genre(name = "irrelevant name", id = "roman"),
                    AnimeQuery.Genre(name = "irrelevant name", id = "act"),
                ),
                1,
            ),
            Arguments.of(
                AnimeFilter(
                    genre = AnimeFilter.Genre(genre = "action", genreId = "act")
                ),
                listOf(
                    AnimeQuery.Genre(name = "irrelevant name", id = "roman"),
                    AnimeQuery.Genre(name = "irrelevant name", id = "mystery"),
                    AnimeQuery.Genre(name = "irrelevant name", id = "act"),
                ),
                2,
            ),
        )
    }
}
