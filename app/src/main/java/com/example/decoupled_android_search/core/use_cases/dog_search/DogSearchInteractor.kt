package com.example.decoupled_android_search.core.use_cases.dog_search

class DogSearchInteractor: DogSearchUseCase {
    override fun getBreeds(): List<Breed> {
        TODO("Not yet implemented")
    }

    override fun getSubBreeds(breed: Breed): List<SubBreed> {
        TODO("Not yet implemented")
    }

    override fun getImages(breed: Breed, page: Int) {
        TODO("Not yet implemented")
    }

    override fun getImages(breed: Breed, subBreed: SubBreed, page: Int) {
        TODO("Not yet implemented")
    }
}