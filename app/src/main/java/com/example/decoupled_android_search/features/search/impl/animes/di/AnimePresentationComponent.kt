package com.example.decoupled_android_search.features.search.impl.animes.di

import com.example.decoupled_android_search.features.search.impl.animes.ui.search_results.view.fragment.AnimeSearchFragment
import dagger.Subcomponent

@Subcomponent(modules = [AnimePresentationModule::class])
interface AnimePresentationComponent {
    fun inject(fragment: AnimeSearchFragment)
}