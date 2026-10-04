package com.example.plugins

import com.example.database.BooksTable
import com.example.database.DatabaseFactory
import com.example.database.UsersTable
import io.ktor.server.application.*
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureDatabase() {

    DatabaseFactory.init()

    transaction {
        SchemaUtils.create(
            UsersTable,
            BooksTable
        )
    }
}