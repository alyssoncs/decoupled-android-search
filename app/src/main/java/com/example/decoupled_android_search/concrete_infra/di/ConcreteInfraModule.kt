package com.example.decoupled_android_search.concrete_infra.di

import com.example.decoupled_android_search.BuildConfig
import com.example.decoupled_android_search.concrete_infra.di.qualifiers.DogApiRetrofit
import com.example.decoupled_android_search.concrete_infra.di.qualifiers.JinkanApiRetrofit
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object ConcreteInfraModule {
    @Provides
    @Singleton
    @DogApiRetrofit
    fun provideDogApiRetrofit(): Retrofit {
        return createRetrofit(BuildConfig.DOG_API_BASE_URL)
    }

    @Provides
    @Singleton
    @JinkanApiRetrofit
    fun provideJinkanApiRetrofit(): Retrofit {
        return createRetrofit(BuildConfig.JIKAN_BASE_URL)
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(formatUrl(baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()
    }

    private fun formatUrl(baseUrl: String) = "$baseUrl/"

    private fun createHttpClient() = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
}