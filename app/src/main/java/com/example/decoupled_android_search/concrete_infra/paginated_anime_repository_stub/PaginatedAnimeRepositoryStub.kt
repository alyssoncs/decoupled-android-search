package com.example.decoupled_android_search.concrete_infra.paginated_anime_repository_stub

import com.example.decoupled_android_search.core.use_cases.anime_search.Anime
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeQuery
import com.example.decoupled_android_search.core.use_cases.anime_search.infra.PaginatedAnimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.URL

class PaginatedAnimeRepositoryStub: PaginatedAnimeRepository {
    override fun getRatings(): List<AnimeQuery.Rating> {
        runBlocking {
            delay(2000L)
        }

        return listOf(
            AnimeQuery.Rating("all ages", "all"),
            AnimeQuery.Rating("children", "children"),
            AnimeQuery.Rating("13+", "13"),
            AnimeQuery.Rating("17+", "17"),
            AnimeQuery.Rating("mild nudity", "mild_n"),
            AnimeQuery.Rating("hentai", "hen"),
        )
    }

    override fun getGenres(): List<AnimeQuery.Genre> {
        runBlocking {
            delay(2000L)
        }

        return listOf(
            AnimeQuery.Genre("action", ""),
            AnimeQuery.Genre("adventure", ""),
            AnimeQuery.Genre("cars", ""),
            AnimeQuery.Genre("comedy", ""),
            AnimeQuery.Genre("dementia", ""),
            AnimeQuery.Genre("demons", ""),
            AnimeQuery.Genre("mystery", ""),
            AnimeQuery.Genre("drama", ""),
            AnimeQuery.Genre("ecchi", ""),
            AnimeQuery.Genre("fantasy", ""),
        )
    }

    override fun getAnimes(query: AnimeQuery, page: Int): List<Anime> {
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