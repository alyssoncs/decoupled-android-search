package com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.di

import com.example.decoupled_android_search.concrete_infra.di.qualifiers.JinkanApiRetrofit
import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.AnimeEndpoints
import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.RemotePaginatedAnimeRepositoryAdapter
import com.example.decoupled_android_search.core.use_cases.anime_search.infra.PaginatedAnimeRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object AnimeRepositoryModule {
    @Provides
    @Singleton
    fun provideAnimeEndpoints(@JinkanApiRetrofit retrofit: Retrofit): AnimeEndpoints =
        retrofit.create(AnimeEndpoints::class.java)

    @Provides
    @Singleton
    fun providePaginatedAnimeRepository(endpoints: AnimeEndpoints): PaginatedAnimeRepository =
        RemotePaginatedAnimeRepositoryAdapter(endpoints)
}