package com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints

import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.service_model.AnimeSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimeEndpoints {
    @GET("search/anime")
    fun searchAnime(
        @Query("q") queryString: String,
        @Query("page") oneBasedPage: Int,
        @Query("status") status: String? = null,
        @Query("rated") rated: String? = null,
        @Query("genre") genre: String? = null,
    ): Call<AnimeSearchResponse>
}