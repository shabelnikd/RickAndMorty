package com.shabelnikd.rickandmorty.ui.models

data class AllFilters(
    val query: String,
    val status: String?,
    val gender: String?,
    val species: String?,
    val type: String?
)
