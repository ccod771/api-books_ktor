package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtService {

    private const val SECRET = "minha-chave-secreta-super-segura"

    private const val ISSUER = "api-books"

    private const val AUDIENCE = "api-books-users"

    private const val EXPIRATION_TIME = 1000L * 60 * 60

    private val algorithm = Algorithm.HMAC256(SECRET)

    fun generateToken(
        userId: Int,
        email: String
    ): String {

        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withExpiresAt(
                Date(System.currentTimeMillis() + EXPIRATION_TIME)
            )
            .sign(algorithm)
    }
}