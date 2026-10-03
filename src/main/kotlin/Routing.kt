package com.example

import com.example.models.UserCreateRequest
import com.example.repository.UserRepository
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    routing {

        get("/") {
            call.respondText("API Books")
        }

        route("/users") {

            post {
                val request = call.receive<UserCreateRequest>()

                val user = userService.create(request)

                call.respond(
                    HttpStatusCode.Created,
                    user
                )
            }

            get {
                val users = userService.findAll()

                call.respond(users)
            }

            get("/{id}") {

                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "ID inválido"
                    )
                    return@get
                }

                val user = userService.findById(id)

                if (user == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Usuário não encontrado"
                    )
                    return@get
                }

                call.respond(user)
            }

            put("/{id}") {

                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "ID inválido"
                    )
                    return@put
                }

                val request = call.receive<UserCreateRequest>()

                val user = userService.update(id, request)

                if (user == null) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Usuário não encontrado"
                    )
                    return@put
                }

                call.respond(user)
            }

            delete("/{id}") {

                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "ID inválido"
                    )
                    return@delete
                }

                val deleted = userService.delete(id)

                if (!deleted) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "Usuário não encontrado"
                    )
                    return@delete
                }

                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}