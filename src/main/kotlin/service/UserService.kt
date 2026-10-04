package com.example.service

import com.example.models.User
import com.example.models.UserCreateRequest
import com.example.models.UserLoginRequest
import com.example.repository.UserRepository
import com.example.security.JwtService
import com.example.security.PasswordHasher

class UserService(
    private val repository: UserRepository
) {

    fun create(request: UserCreateRequest): User {

        val hashedPassword = PasswordHasher.hash(request.password)

        val requestWithHash = request.copy(
            password = hashedPassword
        )

        return repository.create(requestWithHash)
    }

    fun findAll(): List<User> {
        return repository.findAll()
    }

    fun findById(id: Int): User? {
        return repository.findById(id)
    }

    fun update(
        id: Int,
        request: UserCreateRequest
    ): User? {

        val hashedPassword = PasswordHasher.hash(request.password)

        val requestWithHash = request.copy(
            password = hashedPassword
        )

        return repository.update(
            id,
            requestWithHash
        )
    }

    fun delete(id: Int): Boolean {
        return repository.delete(id)
    }

    fun login(request: UserLoginRequest): String {

        val user = repository.findByEmail(request.email)
            ?: throw IllegalArgumentException("E-mail ou senha inválidos")

        val passwordValid = PasswordHasher.verify(
            request.password,
            user.password
        )

        if (!passwordValid) {
            throw IllegalArgumentException("E-mail ou senha inválidos")
        }

        return JwtService.generateToken(
            userId = user.id,
            email = user.email
        )
    }
}