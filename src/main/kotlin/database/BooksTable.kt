package com.example.database

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object BooksTable : IntIdTable("books") {

    val category = varchar("category", 100)

    val name = varchar("name", 200)

    val yearRelease = integer("year_release")

    val description = text("description")

    val author = varchar("author", 150)

    val pages = integer("pages")

    val used = bool("used")

    val userId = integer("user_id")
}