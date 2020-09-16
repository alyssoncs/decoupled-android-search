package com.example.decoupled_android_search.core.use_cases.anime_search.infra

import com.example.decoupled_android_search.core.use_cases.anime_search.Anime
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeFilter

interface PaginatedAnimeRepository {
    fun getRatings():List<AnimeFilter.Rate>
    fun getGenres(): List<AnimeFilter.Genre>
    fun getAnimes(filter: AnimeFilter, page: Int): List<Anime>
}
