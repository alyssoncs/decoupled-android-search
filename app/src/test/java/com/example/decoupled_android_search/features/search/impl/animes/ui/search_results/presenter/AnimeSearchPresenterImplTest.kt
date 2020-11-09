package com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter

import com.example.decoupled_android_search.core.use_cases.anime_search.Anime
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.features.search.impl.animes.filter.AnimeFilter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.AnimeSearchView
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.then
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
import java.net.URL
import java.util.stream.Stream

@ExtendWith(MockitoExtension::class)
internal class AnimeSearchPresenterImplTest {
    @Mock
    private lateinit var useCase: AnimeSearchUseCase

    @Mock
    private lateinit var view: AnimeSearchView

    private lateinit var presenter: AnimeSearchPresenter

    private lateinit var filter: AnimeFilter
    private lateinit var query: AnimeQuery

    @BeforeEach
    internal fun setUp() {

        filter = AnimeFilter(
            name = "adventure",
            status = AnimeQuery.Status.AIRING
        )

        query = AnimeQuery(
            name = filter.name,
            status = filter.status
        )

        presenter = AnimeSearchPresenterImpl(useCase, filter).apply {
            setView(view)
        }
    }

    @Test
    fun `given valid filter, on start, then show loading animation`() {
        presenter.onStart()

        then(view)
            .should()
            .showLoadingAnimation()
    }

    @Test
    fun `given valid filter, on start, then show and hide loading animation`() {
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
    fun `given valid filter and successful use case fetch, on start, then update anime results`(animes: List<Anime>) {
        given(useCase.get(query, 0))
            .willReturn(animes)

        presenter.onStart()

        then(view)
            .should()
            .updateAnimeList(animes)
    }
}

class AnimesProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        Arguments.of(
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
            listOf(
                Anime(
                    name = "digmon adventure",
                    imageUrl = URL("http://www.google.com.br"),
                    synopsis = "",
                    episodes = 127,
                    score = 5.6
                ),
            ),
        )
    )
}

