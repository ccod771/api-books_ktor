package com.example.repository

import com.example.database.UsersTable
import com.example.models.User
import com.example.models.UserCreateRequest
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserRepository {

    fun create(request: UserCreateRequest): User {

        return transaction {

            val statement = UsersTable.insert {
                it[UsersTable.name] = request.name
                it[UsersTable.email] = request.email
                it[UsersTable.password] = request.password
            }

            val id = statement[UsersTable.id].value

            User(
                id = id,
                name = request.name,
                email = request.email
            )
        }
    }

    fun findAll(): List<User> {

        return transaction {

            UsersTable
                .selectAll()
                .map { row ->
                    User(
                        id = row[UsersTable.id].value,
                        name = row[UsersTable.name],
                        email = row[UsersTable.email]
                    )
                }
        }
    }

    fun findById(id: Int): User? {

        return transaction {

            UsersTable
                .selectAll()
                .where { UsersTable.id eq id }
                .map { row ->
                    User(
                        id = row[UsersTable.id].value,
                        name = row[UsersTable.name],
                        email = row[UsersTable.email]
                    )
                }
                .singleOrNull()
        }
    }

    fun update(id: Int, request: UserCreateRequest): User? {

        return transaction {

            val updatedRows = UsersTable.update(
                where = { UsersTable.id eq id }
            ) {
                it[UsersTable.name] = request.name
                it[UsersTable.email] = request.email
                it[UsersTable.password] = request.password
            }

            if (updatedRows == 0) {
                return@transaction null
            }

            User(
                id = id,
                name = request.name,
                email = request.email
            )
        }
    }

    fun delete(id: Int): Boolean {

        return transaction {

            UsersTable.deleteWhere {
                UsersTable.id eq id
            } > 0
        }
    }
}