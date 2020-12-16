package com.example.decoupled_android_search.features.search.impl.dogs.filter

import com.example.decoupled_android_search.features.search.contract.SearchFilter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DogFilter(
    var breed: String = "",
    var subBreed: String = ""
) : SearchFilter() {
    companion object {
        fun createEmpty() = DogFilter()
    }

    override fun isEmpty(): Boolean {
        return breed.isEmpty() && subBreed.isEmpty()
    }

    fun hasSubBreed() = subBreed.isNotBlank()
}