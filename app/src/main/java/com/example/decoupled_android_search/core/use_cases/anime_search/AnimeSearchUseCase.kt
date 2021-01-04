package com.example.decoupled_android_search.core.use_cases.anime_search

import com.example.decoupled_android_search.core.entities.Anime

interface AnimeSearchUseCase {
    fun getStatus(): List<AnimeQuery.Status> = AnimeQuery.Status.values().asList()
    fun getRatings(): List<AnimeQuery.Rating>
    fun getGenres(): List<AnimeQuery.Genre>
    fun get(query: AnimeQuery, page: Int): List<Anime>

    class SearchException: Throwable()
}

data class AnimeQuery(
    val name: String,
    val status: Status? = null,
    val rated: Rating? = null,
    val genre: Genre? = null,
) {
    enum class Status {
        AIRING,
        COMPLETED,
        TO_BE_AIRED
    }

    data class Rating(
        val name: String,
        val id: String
    )

    data class Genre(
        val name: String,
        val id: String
    )
}
