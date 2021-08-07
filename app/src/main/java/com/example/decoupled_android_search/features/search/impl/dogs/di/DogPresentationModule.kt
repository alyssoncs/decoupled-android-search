package com.example.decoupled_android_search.features.search.impl.dogs.di

import com.example.decoupled_android_search.core.use_cases.dog_search.DogSearchUseCase
import com.example.decoupled_android_search.features.search.impl.dogs.filter.DogFilter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter.DogFilterPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_filter.presenter.DogFilterPresenterImpl
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.presenter.DogSearchPresenter
import com.example.decoupled_android_search.features.search.impl.dogs.ui.search_results.presenter.DogSearchPresenterImpl
import dagger.Module
import dagger.Provides

@Module
object DogPresentationModule {
    @Provides
    fun provideDogSearchPresenter(useCase: DogSearchUseCase, filter: DogFilter): DogSearchPresenter =
        DogSearchPresenterImpl(useCase, filter)

    @Provides
    fun provideDogFilterPresenter(useCase: DogSearchUseCase): DogFilterPresenter =
        DogFilterPresenterImpl(useCase)
}