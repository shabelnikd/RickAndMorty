package com.shabelnikd.rickandmorty.domain.models.characters

data class Info(
    val count: Int,
    val pages: Int,
    val next: String? = null,
    val prev: String? = null
)