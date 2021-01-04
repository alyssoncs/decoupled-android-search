package com.example.decoupled_android_search.core.entities

import java.net.URL

data class Anime(
    val name: String,
    val imageUrl: URL,
    val synopsis: String,
    val episodes: Int,
    val score: Double
)