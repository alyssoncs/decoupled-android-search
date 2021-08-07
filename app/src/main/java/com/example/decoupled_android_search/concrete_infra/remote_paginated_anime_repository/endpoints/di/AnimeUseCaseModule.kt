package com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.di

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchInteractor
import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.core.use_cases.anime_search.infra.PaginatedAnimeRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AnimeUseCaseModule {
    @Provides
    @Singleton
    fun provideAnimeSearchUseCase(repository: PaginatedAnimeRepository): AnimeSearchUseCase =
        AnimeSearchInteractor(repository)
}