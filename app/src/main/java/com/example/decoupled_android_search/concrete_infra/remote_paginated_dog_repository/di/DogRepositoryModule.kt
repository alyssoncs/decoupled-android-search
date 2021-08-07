package com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.di

import com.example.decoupled_android_search.concrete_infra.di.qualifiers.DogApiRetrofit
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.RemotePaginatedDogRepositoryAdapter
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.DogsEndpoints
import com.example.decoupled_android_search.core.use_cases.dog_search.infra.PaginatedDogRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object DogRepositoryModule {
    @Provides
    @Singleton
    fun provideDogsEndpoints(@DogApiRetrofit retrofit: Retrofit): DogsEndpoints =
        retrofit.create(DogsEndpoints::class.java)

    @Provides
    @Singleton
    fun providePaginatedDogRepository(endpoints: DogsEndpoints): PaginatedDogRepository =
        RemotePaginatedDogRepositoryAdapter(endpoints)
}