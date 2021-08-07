package com.example.decoupled_android_search.concrete_infra.di;

import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.di.DogRepositoryModule
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.di.DogUseCaseModule
import com.example.decoupled_android_search.features.search.impl.dogs.di.DogPresentationComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ConcreteInfraModule::class,
    DogRepositoryModule::class,
    DogUseCaseModule::class,
])
@Singleton
interface ConcreteInfraComponent {
    fun dogPresentationComponentBuilder(): DogPresentationComponent.Builder
}
