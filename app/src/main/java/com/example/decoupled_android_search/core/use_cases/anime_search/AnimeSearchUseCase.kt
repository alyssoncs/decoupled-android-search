package com.example.decoupled_android_search.core.use_cases.anime_search

import com.example.decoupled_android_search.core.entities.Anime

interface AnimeSearchUseCase {
    fun getStatus(): List<AnimeQuery.Status> = AnimeQuery.Status.values().asList()
    fun getRatings(): List<AnimeQuery.Rating>
    fun getGenres(): List<AnimeQuery.Genre>
    fun get(query: AnimeQuery, page: Int): List<Anime>

    class SearchException: Throwable()
}

data class AnimeQuery(
    val name: String,
    val status: Status? = null,
    val rated: Rating? = null,
    val genre: Genre? = null,
) {
    enum class Status {
        AIRING,
        COMPLETED,
        TO_BE_AIRED
    }

    data class Rating(
        val name: String,
        val id: String
    )

    data class Genre(
        val name: String,
        val id: String
    )
}


/*
enum class Rate {
    ALL_AGES,
    CHILDREN,
    THIRTEEN_PLUS,
    SEVENTEEN_PLUS,
    NUDITY,
    HENTAI,
}
*/
/*
enum class Genre {
    ACTION,
    ADVENTURE,
    CARS,
    COMEDY,
    DEMENTIA,
    DEMONS,
    MYSTERY,
    DRAMA,
    ECCHI,
    FANTASY,
    GAME,
    HENTAI,
    HISTORICAL,
    HORROR,
    KIDS,
    MAGIC,
    MARTIAL_ARTS,
    MECHA,
    MUSIC,
    PARODY,
    SAMURAI,
    ROMANCE,
    SCHOOL,
    SCI_FI,
    SHOUJO,
    SHOUJO_AI,
    SHOUNEN,
    SHOUNEN_AI,
    SPACE,
    SPORTS,
    SUPER_POWER,
    VAMPIRE,
    YAOI,
    YURI,
    HAREM,
    SLICE_OF_LIFE,
    SUPERNATURAL,
    MILITARY,
    POLICE,
    PSYCHOLOGICAL,
    THRILLER,
    SEINEN,
    JOSEI,
}
*/

/*
Action: 1
Adventure: 2
Cars: 3
Comedy: 4
Dementia: 5
Demons: 6
Mystery: 7
Drama: 8
Ecchi: 9
Fantasy: 1
Game: 1
Hentai: 1
Historical: 1
Horror: 1
Kids: 1
Magic: 1
Martial Arts: 1
Mecha: 1
Music: 1
Parody: 2
Samurai: 2
Romance: 2
School: 2
Sci Fi: 2
Shoujo: 2
Shoujo Ai: 2
Shounen: 2
Shounen Ai: 2
Space: 2
Sports: 3
Super Power: 3
Vampire: 3
Yaoi: 3
Yuri: 3
Harem: 3
Slice Of Life: 3
Supernatural: 3
Military: 3
Police: 3
Psychological: 4
Thriller: 4
Seinen: 4
Josei: 4


*/
