package com.example.decoupled_android_search.core.use_cases.anime_search

import com.example.decoupled_android_search.core.use_cases.anime_search.infra.AnimeRepository
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.given
import org.junit.jupiter.api.BeforeEach
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
class AnimeSearchInteractorTest {
    @Mock
    lateinit var repository: AnimeRepository

    lateinit var useCase: AnimeSearchUseCase

    @BeforeEach
    internal fun setUp() {
        useCase = AnimeSearchInteractor(repository)
    }

    @ParameterizedTest
    @ArgumentsSource(RatingsProvider::class)
    fun `should get rates from repository`(expectedRatings: List<AnimeFilter.Rate>) {
        given(repository.getRatings())
            .willReturn(expectedRatings)

        val actualRatings = useCase.getRates()

        thenRatingsAreEqual(actualRatings, expectedRatings)
    }

    @ParameterizedTest
    @ArgumentsSource(GenresProvider::class)
    fun `should get genres from repository`(expectedGenres: List<AnimeFilter.Genre>) {
        given(repository.getGenres())
            .willReturn(expectedGenres)

        val actualGenres = useCase.getGenres()

        thenGenresAreEqual(actualGenres, expectedGenres)
    }

    @ParameterizedTest
    @ArgumentsSource(SearchAndResponseProvider::class)
    fun `should get animes from repository`(
        filter: AnimeFilter,
        page: Int,
        response: List<Anime>
    ) {
        given(repository.getAnimes(filter, page))
            .willReturn(response)

        val animes = useCase.get(filter, page)

        assertThat(animes).isEqualTo(response)
    }

    private fun thenRatingsAreEqual(
        actualRatings: List<AnimeFilter.Rate>,
        expectedRatings: List<AnimeFilter.Rate>
    ) {
        assertThat(actualRatings.size).isEqualTo(expectedRatings.size)
        for (i in actualRatings.indices)
            assertThat(actualRatings[i].name).isEqualTo(expectedRatings[i].name)
    }

    private fun thenGenresAreEqual(
        actualGenres: List<AnimeFilter.Genre>,
        expectedGenres: List<AnimeFilter.Genre>
    ) {
        assertThat(actualGenres.size).isEqualTo(expectedGenres.size)
        for (i in actualGenres.indices)
            assertThat(actualGenres[i].name).isEqualTo(expectedGenres[i].name)
    }

}

class RatingsProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                listOf(
                    object: AnimeFilter.Rate {
                        override val name = "18+"
                    }
                )
            ),
            Arguments.of(
                listOf(
                    object: AnimeFilter.Rate {
                        override val name = "18+"
                    },
                    object: AnimeFilter.Rate {
                        override val name = "Children"
                    }
                )
            ),
            Arguments.of(
                emptyList<AnimeFilter.Rate>()
            ),
        )
    }

}

class GenresProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                emptyList<AnimeFilter.Genre>()
            ),
            Arguments.of(
                listOf(
                    object: AnimeFilter.Genre {
                        override val name = "action"
                    },
                    object: AnimeFilter.Genre {
                        override val name = "romance"
                    },
                )
            ),
        )
    }
}

class SearchAndResponseProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                AnimeFilter(
                    "death note"
                ),
                0,
                listOf(
                    Anime(
                        name = "Death Note",
                        imageUrl = URL("https://www.google.com"),
                        synopsis = "empty",
                        episodes = 50,
                        score = 9.8
                    )
                )
            ),
            Arguments.of(
                AnimeFilter(
                    "death note"
                ),
                1,
                emptyList<Anime>()
            ),
        )
    }

}

