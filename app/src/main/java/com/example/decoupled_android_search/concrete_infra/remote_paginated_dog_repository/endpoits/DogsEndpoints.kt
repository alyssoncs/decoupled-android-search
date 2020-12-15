package com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits

import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.service_model.AllBreedImagesResponse
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.service_model.AllBreedsResponse
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.service_model.AllSubBreedsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsEndpoints {
    @GET("breeds/list/all")
    fun getAllBreeds(): Call<AllBreedsResponse>

    @GET("breed/{breed}/list")
    fun getAllSubBreedsFrom(@Path("breed") breed: String): Call<AllSubBreedsResponse>

    @GET("breed/{breed}/images")
    fun getBreedImages(@Path("breed") breed: String): Call<AllBreedImagesResponse>

    @GET("breed/{breed}/{subBreed}/images")
    fun getSubBreedImages(
        @Path("breed") breed: String,
        @Path("subBreed") subBreed: String
    ): Call<AllBreedImagesResponse>
}