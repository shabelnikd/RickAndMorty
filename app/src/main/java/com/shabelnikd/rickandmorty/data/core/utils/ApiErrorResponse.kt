package com.shabelnikd.rickandmorty.data.core.utils

data class ApiErrorResponse(
    val code: Int? = null,
    val message: String? = null,
    val errors: Map<String, List<String>>? = null
)