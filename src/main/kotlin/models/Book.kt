package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val category: String,
    val name: String,
    val year_release: Int,
    val description: String,
    val author: String,
    val pages: Int,
    val used: Boolean,
    val userId: Int
)