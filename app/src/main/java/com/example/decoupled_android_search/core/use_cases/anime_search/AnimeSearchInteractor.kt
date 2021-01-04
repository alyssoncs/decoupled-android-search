package com.example.decoupled_android_search.core.use_cases.anime_search

import com.example.decoupled_android_search.core.entities.Anime
import com.example.decoupled_android_search.core.use_cases.anime_search.infra.PaginatedAnimeRepository

class AnimeSearchInteractor(
    private val repository: PaginatedAnimeRepository
) : AnimeSearchUseCase {

    override fun getRatings(): List<AnimeQuery.Rating> {
        return try {
            repository.getRatings()
        } catch (e: PaginatedAnimeRepository.SearchException) {
            throw AnimeSearchUseCase.SearchException()
        }
    }

    override fun getGenres(): List<AnimeQuery.Genre> {
        return try {
            repository.getGenres()
        } catch (e: PaginatedAnimeRepository.SearchException) {
            throw AnimeSearchUseCase.SearchException()
        }
    }

    override fun get(query: AnimeQuery, page: Int): List<Anime> {
        return try {
            repository.getAnimes(query, page)
        } catch (e: PaginatedAnimeRepository.SearchException) {
            throw AnimeSearchUseCase.SearchException()
        }
    }
}