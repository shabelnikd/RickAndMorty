package com.shabelnikd.rickandmorty.domain.models.characters

data class BaseResponse(
    val info: Info,
    val results: List<Character>
)