package com.example.decoupled_android_search.core.use_cases.dog_search.infra

import com.example.decoupled_android_search.core.use_cases.dog_search.Breed
import com.example.decoupled_android_search.core.use_cases.dog_search.SubBreed
import java.net.URL

interface PaginatedDogRepository {
    fun getAllBreeds(): List<Breed>
    fun getSubBreeds(breed: Breed): List<SubBreed>
    fun getBreedImagesByPage(breed: Breed, page: Int): List<URL>
    fun getSubBreedImagesByPage(subBreed: SubBreed, page: Int): List<URL>

    class SearchException: Throwable()
}
