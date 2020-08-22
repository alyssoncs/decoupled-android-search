package com.example.decoupled_android_search.features.search.impl.dogs.filter

import android.os.Parcel
import android.os.Parcelable
import com.example.decoupled_android_search.features.search.contract.SearchFilterIntent
import kotlinx.android.parcel.Parcelize

@Parcelize
class DogFilter(
    var breed: String = "",
    var subBreed: String = ""
) : SearchFilterIntent.SearchFilter() {
    companion object {
        fun createEmpty() = DogFilter()
    }

    override fun isEmpty(): Boolean {
        return breed.isEmpty() && subBreed.isEmpty()
    }
}