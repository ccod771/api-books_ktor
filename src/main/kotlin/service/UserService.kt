package com.example.service

import com.example.models.User
import com.example.models.UserCreateRequest
import com.example.repository.UserRepository

class UserService(
    private val repository: UserRepository
) {

    fun create(request: UserCreateRequest): User {
        return repository.create(request)
    }

    fun findAll(): List<User> {
        return repository.findAll()
    }

    fun findById(id: Int): User? {
        return repository.findById(id)
    }

    fun update(id: Int, request: UserCreateRequest): User? {
        return repository.update(id, request)
    }

    fun delete(id: Int): Boolean {
        return repository.delete(id)
    }
}