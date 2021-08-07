package com.example.decoupled_android_search.concrete_infra.di

import com.example.decoupled_android_search.BuildConfig
import com.example.decoupled_android_search.concrete_infra.di.qualifiers.DogApiRetrofit
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
        return Retrofit.Builder()
            .baseUrl("${BuildConfig.DOG_API_BASE_URL}/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()
            )
            .build()
    }
}