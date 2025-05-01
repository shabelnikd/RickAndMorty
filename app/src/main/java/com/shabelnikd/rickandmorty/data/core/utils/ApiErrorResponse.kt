package com.shabelnikd.rickandmorty.data.core.utils

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val code: Int? = null,
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
)