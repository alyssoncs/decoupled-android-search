package com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints

import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.service_model.AnimeApiGenre
import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.service_model.AnimeApiRated
import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.service_model.AnimeApiStatus
import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.service_model.AnimeSearchResponse
import com.example.decoupled_android_search.core.entities.Anime
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.core.use_cases.anime_search.infra.PaginatedAnimeRepository
import retrofit2.Response
import java.io.IOException
import java.net.URL

class RemotePaginatedAnimeRepositoryAdapter(
    private val endpoint: AnimeEndpoints
): PaginatedAnimeRepository {
    override fun getRatings(): List<AnimeQuery.Rating> {
        return AnimeApiRated
            .values()
            .map {
                AnimeQuery.Rating(name = it.name, id = it.value)
            }
    }

    override fun getGenres(): List<AnimeQuery.Genre> {
        return AnimeApiGenre
            .values()
            .map {
                AnimeQuery.Genre(name = it.name, id = it.id.toString())
            }
    }

    override fun getAnimes(query: AnimeQuery, page: Int): List<Anime> {
        val call = endpoint.searchAnime(
            queryString = query.name,
            oneBasedPage = page + 1,
            status = query.status?.let { AnimeApiStatus.fromStatus(it).value },
            rated = query.rated?.id,
            genre = query.genre?.id
        )


        return try {
            mapResponse(call.execute())
        } catch (e: IOException) {
            throw PaginatedAnimeRepository.SearchException()
        } catch (e: RuntimeException) {
            throw PaginatedAnimeRepository.SearchException()
        }
    }

    private fun mapResponse(response: Response<AnimeSearchResponse>): List<Anime> {
        return when {
            response.isSuccessful -> {
                convertResponseBody(response.body()!!)
            }

            response.code() == 404 -> {
                emptyList()
            }

            else -> {
                throw PaginatedAnimeRepository.SearchException()
            }
        }
    }

    private fun convertResponseBody(body: AnimeSearchResponse) =
        body.results
            .map {
                Anime(
                    name = it.title,
                    imageUrl = URL(it.imageUrl),
                    synopsis = it.synopsis,
                    episodes = it.episodes,
                    score = it.score
                )
            }
}