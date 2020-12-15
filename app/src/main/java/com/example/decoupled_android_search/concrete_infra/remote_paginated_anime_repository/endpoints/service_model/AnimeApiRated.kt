package com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.service_model

enum class AnimeApiRated(val value: String) {
    ALL_AGES("g"),
    CHILDREN("pg"),
    TEENS_13_OR_OLDER("pg13"),
    SEVENTEEN_PLUS_RECOMMENDED("r17"),
    MILD_NUDITY("r"),
    HENTAI("rx"),
}