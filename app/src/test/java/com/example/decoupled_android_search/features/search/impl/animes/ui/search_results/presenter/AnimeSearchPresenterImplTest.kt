package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter

import com.example.decoupled_android_search.core.use_cases.anime_search.Anime
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.then
import com.nhaarman.mockitokotlin2.times
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.net.URL
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class)
class AnimeSearchPresenterImplTest {
    @Mock
    private lateinit var useCase: AnimeSearchUseCase

    @Mock
    private lateinit var view: AnimeSearchView

    private lateinit var presenter: AnimeSearchPresenter

    @BeforeEach
    fun setUp() {
        presenter = AnimeSearchPresenterImpl(useCase).apply {
            setView(view)
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AppBarTitleProvider::class)
    fun `given any filter, on start, then change app bar title to anime filter name`(
        givenFilter: AnimeFilter,
        expectedAppBarTitle: String
    ) {
        presenter.setFilter(givenFilter)

        presenter.onStart()

        then(view)
            .should()
            .setAppBarTitle(expectedAppBarTitle)
    }

    @Nested
    @DisplayName("given empty filter")
    inner class EmptyFilter {

        @BeforeEach
        fun setUp() {
            val emptyFilter= AnimeFilter.createEmpty()
            presenter.setFilter(emptyFilter)
        }

        @Test
        fun `on start, then notify invalid filter`() {
            presenter.onStart()

            then(view)
                .should()
                .notifyInvalidFilter()
        }

        @Test
        fun `on start, then don't call the use case`() {
            presenter.onStart()

            then(useCase)
                .should(never())
                .get(any(), any())
        }

        @Test
        fun `on reach end of scroll, then do nothing`() {
            presenter.onStart()

            presenter.onReachEndOfScroll()

            then(useCase)
                .should(never())
                .get(any(), any())
            then(view)
                .should(never())
                .updateAnimeList(any())
        }
    }

    @Nested
    @DisplayName("given valid filter")
    inner class ValidFilter {

        @Test
        fun `on start, then show loading animation`() {
            val filter = AnimeFilter(
                name = "adventure",
                status = AnimeQuery.Status.AIRING
            )
            presenter.setFilter(filter)

            presenter.onStart()

            then(view)
                .should()
                .showLoadingAnimation()
        }

        @Test
        fun `on start, then show and hide loading animation`() {
            val filter = AnimeFilter(
                name = "adventure",
                status = AnimeQuery.Status.AIRING
            )
            presenter.setFilter(filter)

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
        @ArgumentsSource(AnimesProvider::class)
        fun `given successful use case fetch, on start, then update anime results`(
            searchFilter: AnimeFilter,
            expectedAnimes: List<Anime>
        ) {
            presenter.setFilter(searchFilter)
            configureUseCaseToReturn(searchFilter, 0, expectedAnimes)

            presenter.onStart()

            then(view)
                .should()
                .updateAnimeList(expectedAnimes)
        }

        @Test
        fun `given failure on use case fetch, on start, then display search error message`() {
            val filter = AnimeFilter(
                name = "adventure",
                status = AnimeQuery.Status.AIRING
            )
            presenter.setFilter(filter)
            configureUseCaseToFail(filter, 0)

            presenter.onStart()

            then(view)
                .should()
                .displaySearchErrorMessage()
        }

        @Test
        fun `given failure on use case fetch on start, on reach end of scroll, then do nothing`() {
            val filter = AnimeFilter(
                name = "adventure",
                status = AnimeQuery.Status.AIRING
            )
            presenter.setFilter(filter)
            configureUseCaseToFail(filter, 0)
            presenter.onStart()

            presenter.onReachEndOfScroll()

            then(useCase)
                .should(times(1))
                .get(any(), any())

            then(view)
                .should(never())
                .updateAnimeList(any())
        }

        @Test
        fun `given use case fetch success on start and fails on reach end of scroll, on reach end of scroll, then do notify search error`() {
            val filter = AnimeFilter(
                name = "adventure",
                status = AnimeQuery.Status.AIRING
            )
            val animes = listOf(
                Anime(
                    name = "jojo's bizarre adventures",
                    imageUrl = URL("http://www.google.com.br"),
                    synopsis = "",
                    episodes = 150,
                    score = 8.9
                )
            )
            presenter.setFilter(filter)
            configureUseCaseToReturn(filter, 0, animes)
            presenter.onStart()
            configureUseCaseToFail(filter, 1)

            presenter.onReachEndOfScroll()

            then(useCase)
                .should()
                .get(filter.toQuery(), 1)

            then(view)
                .should()
                .displaySearchErrorMessage()
        }

        @Test
        fun `given use case fetch fails on reach end of scroll, on reach end of scroll, then search the previous page`() {
            val filter = AnimeFilter(
                name = "adventure",
                status = AnimeQuery.Status.AIRING
            )
            val animes = listOf(
                listOf(
                    Anime(
                        name = "jojo's bizarre adventures",
                        imageUrl = URL("http://www.google.com.br"),
                        synopsis = "",
                        episodes = 150,
                        score = 8.9
                    ),
                ),
                listOf(
                    Anime(
                        name = "death note",
                        imageUrl = URL("http://www.google.com.br"),
                        synopsis = "",
                        episodes = 29,
                        score = 9.0
                    ),
                ),
                listOf(
                    Anime(
                        name = "naruto",
                        imageUrl = URL("http://www.google.com.br"),
                        synopsis = "",
                        episodes = 290,
                        score = 8.0
                    ),
                ),
            )
            presenter.setFilter(filter)
            configureUseCaseToReturn(filter, 0, animes[0])
            configureUseCaseToReturn(filter, 1, animes[1])
            configureUseCaseToFail(filter, 2)
            presenter.onStart()
            presenter.onReachEndOfScroll()
            presenter.onReachEndOfScroll()
            configureUseCaseToReturn(filter, 2, animes[2])

            presenter.onReachEndOfScroll()

            then(view)
                .should()
                .updateAnimeList(animes[2])
        }
        @Test
        fun `given use case fetch successful, on reach end of scroll, then search the next page`() {
            val filter = AnimeFilter(
                name = "adventure",
                status = AnimeQuery.Status.AIRING
            )
            val animes = listOf(
                listOf(
                    Anime(
                        name = "jojo's bizarre adventures",
                        imageUrl = URL("http://www.google.com.br"),
                        synopsis = "",
                        episodes = 150,
                        score = 8.9
                    ),
                ),
                listOf(
                    Anime(
                        name = "death note",
                        imageUrl = URL("http://www.google.com.br"),
                        synopsis = "",
                        episodes = 29,
                        score = 9.0
                    ),
                ),
                listOf(
                    Anime(
                        name = "naruto",
                        imageUrl = URL("http://www.google.com.br"),
                        synopsis = "",
                        episodes = 290,
                        score = 8.0
                    ),
                ),
            )
            presenter.setFilter(filter)
            configureUseCaseToReturn(filter, 0, animes[0])
            configureUseCaseToReturn(filter, 1, animes[1])
            configureUseCaseToReturn(filter, 2, animes[2])
            presenter.onStart()
            presenter.onReachEndOfScroll()
            presenter.onReachEndOfScroll()


            then(view)
                .should()
                .updateAnimeList(animes[0])
            then(view)
                .should()
                .updateAnimeList(animes[1])
            then(view)
                .should()
                .updateAnimeList(animes[2])
        }

        @Test
        internal fun `given use case returns empty list on start, on reach end of scroll, then don't search anymore on reach end of scroll`() {
            val filter = AnimeFilter(
                name = "adventure",
                status = AnimeQuery.Status.AIRING
            )
            presenter.setFilter(filter)
            configureUseCaseToReturn(filter, 0, emptyList())
            presenter.onStart()
            clearInvocations(useCase)

            presenter.onReachEndOfScroll()

            then(useCase)
                .should(never())
                .get(any(), any())
        }
    }

    private fun configureUseCaseToReturn(filter: AnimeFilter, page: Int, animes: List<Anime>) {
        given(useCase.get(eq(filter.toQuery()), eq(page)))
            .willReturn(animes)
    }

    private fun configureUseCaseToFail(filter: AnimeFilter, page: Int) {
        given(useCase.get(eq(filter.toQuery()), eq(page)))
            .willAnswer { throw AnimeSearchUseCase.SearchException() }
    }
}

class AppBarTitleProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        Arguments.of(
            AnimeFilter(
                name = "naruto",
                status = null
            ),
            "naruto",
        ),
        Arguments.of(
            AnimeFilter(
                name = "naruto",
                status = AnimeQuery.Status.TO_BE_AIRED
            ),
            "naruto",
        ),
        Arguments.of(
            AnimeFilter(
                name = "death note",
                status = AnimeQuery.Status.AIRING,
                rating = AnimeFilter.Rating("+17", "+17")
            ),
            "death note",
        ),
        Arguments.of(
            AnimeFilter.createEmpty(),
            "",
        ),
    )
}

class AnimesProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        Arguments.of(
            AnimeFilter("jojo"),
            listOf(
                Anime(
                    name = "jojo's bizarre adventures",
                    imageUrl = URL("http://www.google.com.br"),
                    synopsis = "",
                    episodes = 150,
                    score = 8.9
                ),
            )
        ),
        Arguments.of(
            AnimeFilter("digimon"),
            listOf(
                Anime(
                    name = "digimon adventure",
                    imageUrl = URL("http://www.google.com.br"),
                    synopsis = "",
                    episodes = 127,
                    score = 5.6
                ),
            ),
        )
    )
}
