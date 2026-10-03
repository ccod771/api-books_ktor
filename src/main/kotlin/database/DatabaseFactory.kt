package com.example.database

import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseFactory {

    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/api_books",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "root"
        )
    }
}