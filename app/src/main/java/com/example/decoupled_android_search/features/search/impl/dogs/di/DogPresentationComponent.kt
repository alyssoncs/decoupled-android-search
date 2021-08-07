package com.example.decoupled_android_search.features.search.impl.dogs.di

import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.view.fragment.DogSearchFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [DogPresentationModule::class])
interface DogPresentationComponent {
    fun inject(fragment: DogSearchFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun addDogFilter(filter: DogFilter): Builder

        fun build(): DogPresentationComponent
    }
}