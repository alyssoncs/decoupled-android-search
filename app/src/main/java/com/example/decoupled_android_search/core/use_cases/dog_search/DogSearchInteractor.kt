package com.example.decoupled_android_search.core.use_cases.dog_search

import com.example.decoupled_android_search.core.entities.Breed
import com.example.decoupled_android_search.core.entities.SubBreed
import com.example.decoupled_android_search.core.use_cases.dog_search.infra.PaginatedDogRepository
import java.net.URL

class DogSearchInteractor(
    val repository: PaginatedDogRepository
): DogSearchUseCase {

    override fun getBreeds(): List<Breed> {
        return try {
            repository.getAllBreeds()
        } catch (e: PaginatedDogRepository.SearchException) {
            throw DogSearchUseCase.SearchException()
        }
    }

    override fun getSubBreeds(breed: Breed): List<SubBreed> {
        return try {
            repository.getSubBreeds(breed)
        } catch (e: PaginatedDogRepository.SearchException) {
            throw DogSearchUseCase.SearchException()
        }
    }

    override fun getImages(breed: Breed, page: Int): List<URL> {
        return try {
            repository.getBreedImagesByPage(breed, page)
        } catch (e: PaginatedDogRepository.SearchException) {
            throw DogSearchUseCase.SearchException()
        }
    }

    override fun getImages(subBreed: SubBreed, page: Int): List<URL> {
        return try {
            repository.getSubBreedImagesByPage(subBreed, page)
        } catch (e: PaginatedDogRepository.SearchException) {
            throw DogSearchUseCase.SearchException()
        }
    }

}