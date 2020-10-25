package com.example.decoupled_android_search.core.use_cases.anime_search.infra

import com.example.decoupled_android_search.core.use_cases.anime_search.Anime
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery

interface PaginatedAnimeRepository {
    fun getRatings():List<AnimeQuery.Rating>
    fun getGenres(): List<AnimeQuery.Genre>
    fun getAnimes(query: AnimeQuery, page: Int): List<Anime>

    class SearchException: Throwable()
}
