package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class BookCreateRequest(
    val category: String,
    val name: String,
    val year_release: Int,
    val description: String,
    val author: String,
    val pages: Int,
    val used: Boolean
)