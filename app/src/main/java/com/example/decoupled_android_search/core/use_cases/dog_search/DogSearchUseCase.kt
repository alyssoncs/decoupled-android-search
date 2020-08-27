package com.example.decoupled_android_search.core.use_cases.dog_search

import java.net.URL

interface DogSearchUseCase {
    fun getBreeds(): List<Breed>
    fun getSubBreeds(breed: Breed): List<SubBreed>
    fun getImages(breed: Breed, page: Int): List<URL>
    fun getImages(subBreed: SubBreed, page: Int): List<URL>

    class SearchException: Throwable()
}

data class Breed(val name: String)
data class SubBreed(val breed: Breed, val name: String)
