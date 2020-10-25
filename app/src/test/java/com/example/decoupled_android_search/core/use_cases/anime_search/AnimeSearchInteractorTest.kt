package com.example.decoupled_android_search.core.use_cases.anime_search

import com.example.decoupled_android_search.core.use_cases.anime_search.infra.PaginatedAnimeRepository
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.given
import org.junit.jupiter.api.Assertions.assertThrows
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
class AnimeSearchInteractorTest {
    @Mock
    lateinit var repository: PaginatedAnimeRepository

    lateinit var useCase: AnimeSearchUseCase

    @BeforeEach
    internal fun setUp() {
        useCase = AnimeSearchInteractor(repository)
    }

    @Test
    fun `should get status from status enumeration`() {
        val expectedStatus = AnimeQuery.Status.values()
        val actualStatus = useCase.getStatus()

        thenStatusAreEqual(actualStatus, expectedStatus)
    }

    @ParameterizedTest
    @ArgumentsSource(RatingsProvider::class)
    fun `should get rates from repository`(expectedRatings: List<AnimeQuery.Rating>) {
        given(repository.getRatings())
            .willReturn(expectedRatings)

        val actualRatings = useCase.getRatings()

        thenRatingsAreEqual(actualRatings, expectedRatings)
    }

    @ParameterizedTest
    @ArgumentsSource(GenresProvider::class)
    fun `should get genres from repository`(expectedGenres: List<AnimeQuery.Genre>) {
        given(repository.getGenres())
            .willReturn(expectedGenres)

        val actualGenres = useCase.getGenres()

        thenGenresAreEqual(actualGenres, expectedGenres)
    }

    @ParameterizedTest
    @ArgumentsSource(SearchAndResponseProvider::class)
    fun `should get animes from repository`(
        query: AnimeQuery,
        page: Int,
        response: List<Anime>
    ) {
        given(repository.getAnimes(query, page))
            .willReturn(response)

        val animes = useCase.get(query, page)

        assertThat(animes).isEqualTo(response)
    }

    @Test
    fun `given that repository fails, when searching ratings, then throw SearchException`() {
        given(repository.getRatings())
            .willAnswer { throw PaginatedAnimeRepository.SearchException() }

        val execute = { useCase.getRatings() }

        thenThrowSearchException(execute)
    }

    @Test
    fun `given that repository fails, when searching genres, then throw SearchException`() {
        given(repository.getGenres())
            .willAnswer { throw PaginatedAnimeRepository.SearchException() }

        val execute = { useCase.getGenres() }

        thenThrowSearchException(execute)
    }

    @Test
    fun `given that repository fails, when searching animes, then throw SearchException`() {
        val query = AnimeQuery(
            "",
            AnimeQuery.Status.AIRING,
            AnimeQuery.Rating("", ""),
            AnimeQuery.Genre("", "")
        )
        val page = 1
        given(repository.getAnimes(query, page))
            .willAnswer { throw PaginatedAnimeRepository.SearchException() }

        val execute = { useCase.get(query, page) }

        thenThrowSearchException(execute)
    }

    private fun thenStatusAreEqual(
        actualStatus: List<AnimeQuery.Status>,
        expectedStatus: Array<AnimeQuery.Status>
    ) {
        assertThat(actualStatus.size).isEqualTo(expectedStatus.size)

        for (i in actualStatus.indices)
            assertThat(actualStatus[i]).isEqualTo(expectedStatus[i])
    }

    private fun thenRatingsAreEqual(
        actualRatings: List<AnimeQuery.Rating>,
        expectedRatings: List<AnimeQuery.Rating>
    ) {
        assertThat(actualRatings.size).isEqualTo(expectedRatings.size)
        for (i in actualRatings.indices)
            assertThat(actualRatings[i].name).isEqualTo(expectedRatings[i].name)
    }

    private fun thenGenresAreEqual(
        actualGenres: List<AnimeQuery.Genre>,
        expectedGenres: List<AnimeQuery.Genre>
    ) {
        assertThat(actualGenres.size).isEqualTo(expectedGenres.size)
        for (i in actualGenres.indices)
            assertThat(actualGenres[i].name).isEqualTo(expectedGenres[i].name)
    }

    private fun thenThrowSearchException(execute: () -> Any) {
        assertThrows(AnimeSearchUseCase.SearchException::class.java) {
            execute()
        }
    }
}

class RatingsProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                listOf(
                    AnimeQuery.Rating(
                        name = "18+",
                        id = "18+"
                    )
                )
            ),
            Arguments.of(
                listOf(
                    AnimeQuery.Rating (
                        name = "18+",
                        id = "18+"
                    ),
                    AnimeQuery.Rating (
                        name = "Children",
                        id = "children"
                    )
                )
            ),
            Arguments.of(
                emptyList<AnimeQuery.Rating>()
            ),
        )
    }

}

class GenresProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                emptyList<AnimeQuery.Genre>()
            ),
            Arguments.of(
                listOf(
                    AnimeQuery.Genre (
                        name = "action",
                        id = "act"
                    ),
                    AnimeQuery.Genre (
                        name = "romance",
                        id = "romance"
                    ),
                )
            ),
        )
    }
}

class SearchAndResponseProvider: ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(
                AnimeQuery(
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
                AnimeQuery(
                    "death note"
                ),
                1,
                emptyList<Anime>()
            ),
        )
    }

}

