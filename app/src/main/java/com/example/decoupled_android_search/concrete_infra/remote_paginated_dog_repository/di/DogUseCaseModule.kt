package com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.di

import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchInteractor
import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchUseCase
import com.example.decoupled_android_search.core.use_cases.dog_search.infra.PaginatedDogRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DogUseCaseModule {
    @Provides
    @Singleton
    fun provideDogSearchUseCase(repository: PaginatedDogRepository): DogSearchUseCase =
        DogSearchInteractor(repository)
}