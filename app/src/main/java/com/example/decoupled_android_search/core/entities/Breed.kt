package com.example.decoupled_android_search.core.entities

data class Breed(val name: String)
data class SubBreed(val breed: Breed, val name: String)
