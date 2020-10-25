package com.example.decoupled_android_search.features.search.impl.animes.filter

import android.os.Parcelable
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.features.search.contract.SearchFilterIntent
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimeFilter(
    val name: String = "",
    val status: AnimeQuery.Status? = null,
    val rating: Rating? = null,
    val genre: Genre? = null,
): SearchFilterIntent.SearchFilter() {
    companion object {
        fun createEmpty() = AnimeFilter()
    }

    override fun isEmpty(): Boolean {
        return name.isEmpty()
    }

    @Parcelize
    data class Rating(val rating: String, val ratingId: String) : Parcelable {
        constructor(rating: AnimeQuery.Rating): this(rating.name, rating.id)
    }

    @Parcelize
    data class Genre(val genre: String, val genreId: String) : Parcelable {
        constructor(genre: AnimeQuery.Genre): this(genre.name, genre.id)
    }
}