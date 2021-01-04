package com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository

import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.DogsEndpoints
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.service_model.AllBreedImagesResponse
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.service_model.AllBreedsResponse
import com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.service_model.AllSubBreedsResponse
import com.example.decoupled_android_search.core.entities.Breed
import com.example.decoupled_android_search.core.entities.SubBreed
import com.example.decoupled_android_search.core.use_cases.dog_search.infra.PaginatedDogRepository
import retrofit2.Response
import java.io.IOException
import java.net.URL

class RemotePaginatedDogRepositoryAdapter(
    private val endpoint: DogsEndpoints
): PaginatedDogRepository {

    companion object {
        private const val PAGE_SIZE = 10
    }

    private var breedImagesCache: Pair<Breed, List<URL>>? = null

    private var subBreedImagesCache: Pair<SubBreed, List<URL>>? = null

    override fun getAllBreeds(): List<Breed> {
        val call = endpoint.getAllBreeds()

        return try {
            mapResponse(call.execute())
        } catch (e: IOException) {
            throw PaginatedDogRepository.SearchException()
        } catch (e: RuntimeException) {
            throw PaginatedDogRepository.SearchException()
        }
    }

    override fun getSubBreeds(breed: Breed): List<SubBreed> {
        val call = endpoint.getAllSubBreedsFrom(breed.name)

        return try {
            mapResponse(breed, call.execute())
        } catch (e: IOException) {
            throw PaginatedDogRepository.SearchException()
        } catch (e: RuntimeException) {
            throw PaginatedDogRepository.SearchException()
        }
    }

    override fun getBreedImagesByPage(breed: Breed, page: Int): List<URL> {
        if (breed != breedImagesCache?.first)
            breedImagesCache = fetchBreedImages(breed)

        return getImagesSubList(breedImagesCache!!.second, page)
    }

    override fun getSubBreedImagesByPage(subBreed: SubBreed, page: Int): List<URL> {
        if (subBreed != subBreedImagesCache?.first)
            subBreedImagesCache = fetchSubBreedImages(subBreed)

        return getImagesSubList(subBreedImagesCache!!.second, page)
    }


    private fun mapResponse(response: Response<AllBreedsResponse>): List<Breed> {
        return if (response.isSuccessful) {
            response.body()!!.message.map { Breed(it.key) }
        } else {
            throw PaginatedDogRepository.SearchException()
        }
    }

    private fun mapResponse(breed: Breed, response: Response<AllSubBreedsResponse>): List<SubBreed> {
        return if (response.isSuccessful) {
            response.body()!!.message.map { subBreedName ->
                SubBreed(breed, subBreedName)
            }
        } else {
            throw PaginatedDogRepository.SearchException()
        }
    }

    private fun fetchBreedImages(breed: Breed): Pair<Breed, List<URL>> {
        val call = endpoint.getBreedImages(breed.name)

        return try {
            Pair(breed, mapImageResponse(call.execute()))
        } catch (e: IOException) {
            throw PaginatedDogRepository.SearchException()
        } catch (e: RuntimeException) {
            throw PaginatedDogRepository.SearchException()
        }
    }

    private fun fetchSubBreedImages(subBreed: SubBreed): Pair<SubBreed, List<URL>> {
        val call = endpoint.getSubBreedImages(subBreed.breed.name, subBreed.name)

        return try {
            Pair(subBreed, mapImageResponse(call.execute()))
        } catch (e: IOException) {
            throw PaginatedDogRepository.SearchException()
        } catch (e: RuntimeException) {
            throw PaginatedDogRepository.SearchException()
        }
    }

    private fun mapImageResponse(response: Response<AllBreedImagesResponse>): List<URL> {
        return if (response.isSuccessful) {
            response.body()!!.message.map { url -> URL(url) }
        } else {
            throw PaginatedDogRepository.SearchException()
        }
    }

    private fun getImagesSubList(images: List<URL>, page: Int): List<URL> {
        val startIndex = PAGE_SIZE * page
        val endIndex = startIndex + PAGE_SIZE

        return when {
            startIndex >= images.size -> emptyList()
            endIndex > images.size -> images.subList(startIndex, images.size)
            else -> images.subList(startIndex, endIndex)
        }
    }
}