package com.example.decoupled_android_search.concrete_infra.remote_paginated_dog_repository.endpoits.service_model

data class AllBreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)