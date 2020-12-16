package com.example.decoupled_android_search.features.search.impl.animes.filter

import android.os.Parcelable
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.features.search.contract.SearchFilter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnimeFilter(
    val name: String = "",
    val status: AnimeQuery.Status? = null,
    val rating: Rating? = null,
    val genre: Genre? = null,
): SearchFilter() {
    companion object {
        fun createEmpty() = AnimeFilter()
    }

    override fun isEmpty(): Boolean {
        return name.isEmpty()
    }

    fun toQuery(): AnimeQuery = AnimeQuery(
        name = name,
        status = status,
        rated = rating?.toQueryRating(),
        genre = genre?.toQueryGenre(),
    )

    @Parcelize
    data class Rating(val rating: String, val ratingId: String) : Parcelable {

        constructor(rating: AnimeQuery.Rating): this(rating.name, rating.id)

        fun toQueryRating() = AnimeQuery.Rating(rating, ratingId)
    }

    @Parcelize
    data class Genre(val genre: String, val genreId: String) : Parcelable {

        constructor(genre: AnimeQuery.Genre): this(genre.name, genre.id)

        fun toQueryGenre() = AnimeQuery.Genre(genre, genreId)
    }
}