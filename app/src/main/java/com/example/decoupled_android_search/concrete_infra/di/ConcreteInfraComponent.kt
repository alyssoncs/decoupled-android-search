package com.example.decoupled_android_search.concrete_infra.di;

import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.di.AnimeRepositoryModule
import com.example.decoupled_android_search.concrete_infra.remote_paginated_anime_repository.endpoints.di.AnimeUseCaseModule
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.di.DogRepositoryModule
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.di.DogUseCaseModule
import com.example.decoupled_android_search.features.search.impl.animes.di.AnimePresentationComponent
import com.example.decoupled_android_search.features.search.impl.dogs.di.DogPresentationComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ConcreteInfraModule::class,
    DogRepositoryModule::class,
    DogUseCaseModule::class,
    AnimeRepositoryModule::class,
    AnimeUseCaseModule::class,
])
@Singleton
interface ConcreteInfraComponent {
    fun dogPresentationComponentBuilder(): DogPresentationComponent.Builder
    fun animePresentationComponent(): AnimePresentationComponent
}
