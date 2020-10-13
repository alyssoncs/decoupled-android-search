package com.example.decoupled_android_search.concrete_infra.paginated_anime_repository_stub

import com.example.decoupled_android_search.core.use_cases.anime_search.Anime
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeFilter
import com.example.decoupled_android_search.core.use_cases.anime_search.infra.PaginatedAnimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.URL

class PaginatedAnimeRepositoryStub: PaginatedAnimeRepository {
    override fun getRatings(): List<AnimeFilter.Rate> {
        runBlocking {
            delay(2000L)
        }

        data class Rate(
            override val name: String
        ): AnimeFilter.Rate

        return listOf(
            Rate("all ages"),
            Rate("children"),
            Rate("13+"),
            Rate("17+"),
            Rate("mild nudity"),
            Rate("hentai"),
        )
    }

    override fun getGenres(): List<AnimeFilter.Genre> {
        runBlocking {
            delay(2000L)
        }

        data class Genre(
            override val name: String
        ): AnimeFilter.Genre

        return listOf(
            Genre("action"),
            Genre("adventure"),
            Genre("cars"),
            Genre("comedy"),
            Genre("dementia"),
            Genre("demons"),
            Genre("mystery"),
            Genre("drama"),
            Genre("ecchi"),
            Genre("fantasy"),
        )
    }

    override fun getAnimes(filter: AnimeFilter, page: Int): List<Anime> {
        runBlocking {
            delay(2000L)
        }

        return listOf(
            Anime(
                name = "pokemon",
                imageUrl = URL("https://img.olhardigital.com.br/uploads/acervo_imagens/2019/05/r16x9/20190507084117_1200_675_-_pokemon.jpg"),
                synopsis = "catch pokemon",
                episodes = 150,
                score = 7.5
            ),
            Anime(
                name = "naruto",
                imageUrl = URL("https://vignette.wikia.nocookie.net/naruto/images/3/33/Naruto_Uzumaki_%28Parte_I_-_HD%29.png/revision/latest?cb=20160316113315&path-prefix=pt-br"),
                synopsis = "become a hokage",
                episodes = 225,
                score = 9.2
            ),
        )
    }
}