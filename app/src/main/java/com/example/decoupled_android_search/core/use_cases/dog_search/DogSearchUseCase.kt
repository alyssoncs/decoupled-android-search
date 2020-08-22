package com.example.decoupled_android_search.core.use_cases.dog_search

interface DogSearchUseCase {
    fun getBreeds(): List<Breed>
    fun getSubBreeds(breed: Breed): List<SubBreed>
    fun getImages(breed: Breed, page: Int)
    fun getImages(breed: Breed, subBreed: SubBreed ,page: Int)
}

data class Breed(val name: String)
data class SubBreed(val name: String)
