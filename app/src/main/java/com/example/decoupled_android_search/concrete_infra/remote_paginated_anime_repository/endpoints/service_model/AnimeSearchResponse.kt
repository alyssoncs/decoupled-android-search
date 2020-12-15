package com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.service_model

import com.google.gson.annotations.SerializedName

data class AnimeSearchResponse(
    val results: List<AnimeSearchItem>
)

data class AnimeSearchItem(
    @SerializedName("image_url")
    val imageUrl: String,
    val title: String,
    val airing: Boolean,
    val synopsis: String,
    val episodes: Int,
    val score: Double
)
