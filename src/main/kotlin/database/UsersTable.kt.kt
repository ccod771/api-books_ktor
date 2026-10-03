package com.example.database

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object UsersTable : IntIdTable("users") {

    val name = varchar("name", 100)

    val email = varchar("email", 150)
        .uniqueIndex()

    val password = varchar("password", 255)
}