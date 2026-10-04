package com.example.repository

import com.example.database.BooksTable
import com.example.models.Book
import com.example.models.BookCreateRequest
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.core.and
class BookRepository {

    fun create(
        request: BookCreateRequest,
        userId: Int
    ): Book {

        return transaction {

            val result = BooksTable.insert {
                it[category] = request.category
                it[name] = request.name
                it[yearRelease] = request.year_release
                it[description] = request.description
                it[author] = request.author
                it[pages] = request.pages
                it[used] = request.used
                it[BooksTable.userId] = userId
            }

            Book(
                id = result[BooksTable.id].value,
                category = result[BooksTable.category],
                name = result[BooksTable.name],
                year_release = result[BooksTable.yearRelease],
                description = result[BooksTable.description],
                author = result[BooksTable.author],
                pages = result[BooksTable.pages],
                used = result[BooksTable.used],
                userId = result[BooksTable.userId]
            )
        }
    }

    fun findAll(): List<Book> {

        return transaction {

            BooksTable
                .selectAll()
                .map { row ->

                    Book(
                        id = row[BooksTable.id].value,
                        category = row[BooksTable.category],
                        name = row[BooksTable.name],
                        year_release = row[BooksTable.yearRelease],
                        description = row[BooksTable.description],
                        author = row[BooksTable.author],
                        pages = row[BooksTable.pages],
                        used = row[BooksTable.used],
                        userId = row[BooksTable.userId]
                    )
                }
        }
    }

    fun findById(id: Int): Book? {

        return transaction {

            BooksTable
                .selectAll()
                .where {
                    BooksTable.id eq id
                }
                .map { row ->

                    Book(
                        id = row[BooksTable.id].value,
                        category = row[BooksTable.category],
                        name = row[BooksTable.name],
                        year_release = row[BooksTable.yearRelease],
                        description = row[BooksTable.description],
                        author = row[BooksTable.author],
                        pages = row[BooksTable.pages],
                        used = row[BooksTable.used],
                        userId = row[BooksTable.userId]
                    )
                }
                .singleOrNull()
        }
    }

    fun update(
        id: Int,
        request: BookCreateRequest
    ): Book? {

        return transaction {

            val updatedRows = BooksTable.update(
                where = {
                    BooksTable.id eq id
                }
            ) {
                it[category] = request.category
                it[name] = request.name
                it[yearRelease] = request.year_release
                it[description] = request.description
                it[author] = request.author
                it[pages] = request.pages
                it[used] = request.used
            }

            if (updatedRows == 0) {
                return@transaction null
            }

            BooksTable
                .selectAll()
                .where {
                    BooksTable.id eq id
                }
                .map { row ->

                    Book(
                        id = row[BooksTable.id].value,
                        category = row[BooksTable.category],
                        name = row[BooksTable.name],
                        year_release = row[BooksTable.yearRelease],
                        description = row[BooksTable.description],
                        author = row[BooksTable.author],
                        pages = row[BooksTable.pages],
                        used = row[BooksTable.used],
                        userId = row[BooksTable.userId]
                    )
                }
                .singleOrNull()
        }
    }
    fun delete(id: Int): Boolean {

        return transaction {

            BooksTable
                .deleteWhere {
                    BooksTable.id eq id
                } > 0
        }
    }

    fun updateByOwner(
        id: Int,
        userId: Int,
        request: BookCreateRequest
    ): Book? {

        return transaction {

            val updatedRows = BooksTable.update(
                where = {
                    (BooksTable.id eq id) and
                            (BooksTable.userId eq userId)
                }
            ) {
                it[category] = request.category
                it[name] = request.name
                it[yearRelease] = request.year_release
                it[description] = request.description
                it[author] = request.author
                it[pages] = request.pages
                it[used] = request.used
            }

            if (updatedRows == 0) {
                return@transaction null
            }

            BooksTable
                .selectAll()
                .where {
                    BooksTable.id eq id
                }
                .map { row ->

                    Book(
                        id = row[BooksTable.id].value,
                        category = row[BooksTable.category],
                        name = row[BooksTable.name],
                        year_release = row[BooksTable.yearRelease],
                        description = row[BooksTable.description],
                        author = row[BooksTable.author],
                        pages = row[BooksTable.pages],
                        used = row[BooksTable.used],
                        userId = row[BooksTable.userId]
                    )
                }
                .singleOrNull()
        }
    }

    fun deleteByOwner(
        id: Int,
        userId: Int
    ): Boolean {

        return transaction {

            BooksTable
                .deleteWhere {
                    (BooksTable.id eq id) and
                            (BooksTable.userId eq userId)
                } > 0
        }
    }

}