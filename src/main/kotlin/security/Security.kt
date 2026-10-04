package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

private const val JWT_SECRET = "minha-chave-secreta-super-segura"
private const val JWT_ISSUER = "api-books"
private const val JWT_AUDIENCE = "api-books-users"

fun Application.configureSecurity() {

    install(Authentication) {

        jwt("auth-jwt") {

            verifier(
                JWT
                    .require(
                        Algorithm.HMAC256(JWT_SECRET)
                    )
                    .withIssuer(JWT_ISSUER)
                    .withAudience(JWT_AUDIENCE)
                    .build()
            )

            validate { credential ->

                val userId = credential
                    .payload
                    .getClaim("userId")
                    .asInt()

                val email = credential
                    .payload
                    .getClaim("email")
                    .asString()

                if (userId != null && email != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->

                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token inválido ou ausente"
                )
            }
        }
    }
}