package com.example.decoupled_android_search.features.search.impl.animes.di

import com.example.decoupled_android_search.core.use_cases.anime_search.AnimeSearchUseCase
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.presenter.AnimeFilterPresenter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.presenter.AnimeFilterPresenterImpl
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_filter.view.activity.AnimeFilterPresenterDispatcher
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter.AnimeSearchPresenter
import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.presenter.AnimeSearchPresenterImpl
import dagger.Module
import dagger.Provides

@Module
object AnimePresentationModule {
    @Provides
    fun provideAnimeSearchPresenter(useCase: AnimeSearchUseCase): AnimeSearchPresenter =
        AnimeSearchPresenterImpl(useCase)

    @Provides
    fun provideAnimeFilterPresenter(useCase: AnimeSearchUseCase): AnimeFilterPresenter =
        AnimeFilterPresenterImpl(useCase)

    @Provides
    fun provideAnimeFilterPresenterDispatcherFactory(
        presenter: AnimeFilterPresenter
    ): AnimeFilterPresenterDispatcher.Factory {
        return AnimeFilterPresenterDispatcher.Factory(presenter)
    }
}