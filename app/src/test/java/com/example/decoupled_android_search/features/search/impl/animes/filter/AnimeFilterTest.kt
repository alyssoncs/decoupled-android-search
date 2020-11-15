package com.example.decoupled_android_search.features.search.impl.animes.filter

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory


internal class AnimeFilterTest {

    @TestFactory
    fun `given anime filter, when converting to anime query, then convert accordingly`() =
        listOf(
            AnimeFilter(name = "naruto", status = null, rating = null, genre = null)
                    to AnimeQuery(name = "naruto", status = null, rated = null, genre = null),

            AnimeFilter(name = "death note", status = AnimeQuery.Status.AIRING, rating = null, genre = null)
                    to AnimeQuery(name = "death note", status = AnimeQuery.Status.AIRING, rated = null, genre = null),

            AnimeFilter(name = "gurren-lagann", status = AnimeQuery.Status.COMPLETED, rating = AnimeFilter.Rating("14+", "14+"), genre = null)
                    to AnimeQuery(name = "gurren-lagann", status = AnimeQuery.Status.COMPLETED, rated = AnimeQuery.Rating("14+", "14+"), genre = null),

            AnimeFilter(name = "dr. stone", status = AnimeQuery.Status.TO_BE_AIRED, rating = AnimeFilter.Rating("14+", "14+"), genre = AnimeFilter.Genre("science", "sci"))
                    to AnimeQuery(name = "dr. stone", status = AnimeQuery.Status.TO_BE_AIRED, rated = AnimeQuery.Rating("14+", "14+"), genre = AnimeQuery.Genre("science", "sci")),
        )
            .map { (givenFilter, expectedQuery) ->
                dynamicTest("given $givenFilter, when converting to anime query, then convert to $expectedQuery") {
                    assertThat(givenFilter.toQuery()).isEqualTo(expectedQuery)
                }
            }

}