package com.example.service

import com.example.models.Book
import com.example.models.BookCreateRequest
import com.example.repository.BookRepository

class BookService(
    private val repository: BookRepository
) {

    fun create(
        request: BookCreateRequest,
        userId: Int
    ): Book {
        return repository.create(
            request = request,
            userId = userId
        )
    }

    fun findAll(): List<Book> {
        return repository.findAll()
    }

    fun findById(id: Int): Book? {
        return repository.findById(id)
    }
}