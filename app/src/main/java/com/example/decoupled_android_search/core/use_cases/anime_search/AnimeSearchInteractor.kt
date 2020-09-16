package com.example.decoupled_android_search.core.use_cases.anime_search

import com.example.decoupled_android_search.core.use_cases.anime_search.infra.PaginatedAnimeRepository

class AnimeSearchInteractor(
    private val repository: PaginatedAnimeRepository
) : AnimeSearchUseCase {

    override fun getRates(): List<AnimeFilter.Rate> {
        return repository.getRatings()
    }

    override fun getGenres(): List<AnimeFilter.Genre> {
        return repository.getGenres()
    }

    override fun get(filter: AnimeFilter, page: Int): List<Anime> {
        return repository.getAnimes(filter, page)
    }
}