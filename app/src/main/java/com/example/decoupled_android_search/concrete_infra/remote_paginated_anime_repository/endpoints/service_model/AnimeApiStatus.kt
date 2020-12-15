package com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.service_model

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery

enum class AnimeApiStatus(val value: String) {
    AIRING("airing"),
    COMPLETED("completed"),
    TO_BE_AIRED("to_be_aired");

    companion object {
        fun fromStatus(s: AnimeQuery.Status): AnimeApiStatus {
            return when (s) {
                AnimeQuery.Status.AIRING -> AIRING
                AnimeQuery.Status.COMPLETED -> COMPLETED
                AnimeQuery.Status.TO_BE_AIRED -> TO_BE_AIRED
            }
        }
    }
}