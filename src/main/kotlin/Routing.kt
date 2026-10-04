package com.example

import com.example.models.BookCreateRequest
import com.example.models.LoginResponse
import com.example.models.UserCreateRequest
import com.example.models.UserLoginRequest
import com.example.repository.BookRepository
import com.example.repository.UserRepository
import com.example.service.BookService
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    val bookRepository = BookRepository()
    val bookService = BookService(bookRepository)

    routing {

        get("/") {
            call.respondText("API Books")
        }

        post("/login") {

            val request = call.receive<UserLoginRequest>()

            try {

                val token = userService.login(request)

                call.respond(
                    LoginResponse(token)
                )

            } catch (e: IllegalArgumentException) {

                call.respond(
                    HttpStatusCode.Unauthorized,
                    e.message ?: "Credenciais inválidas"
                )
            }
        }


        route("/books") {

            get {

                val books = bookService.findAll()

                call.respond(books)
            }
        }



        authenticate("auth-jwt") {



            get("/private") {

                val principal = call.principal<JWTPrincipal>()

                val userId = principal
                    ?.payload
                    ?.getClaim("userId")
                    ?.asInt()

                val email = principal
                    ?.payload
                    ?.getClaim("email")
                    ?.asString()

                call.respond(
                    mapOf(
                        "message" to "Você está autenticado",
                        "userId" to userId,
                        "email" to email
                    )
                )
            }



            route("/users") {

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
                            HttpStatusCode.BadRequest
                        )
                        return@delete
                    }

                    val deleted = userService.delete(id)

                    if (!deleted) {
                        call.respond(
                            HttpStatusCode.NotFound
                        )
                        return@delete
                    }

                    call.respond(HttpStatusCode.NoContent)
                }
            }



            route("/books") {

                post {

                    val principal = call.principal<JWTPrincipal>()

                    val userId = principal
                        ?.payload
                        ?.getClaim("userId")
                        ?.asInt()

                    if (userId == null) {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            "Usuário não autenticado"
                        )
                        return@post
                    }

                    val request = call.receive<BookCreateRequest>()

                    val book = bookService.create(
                        request = request,
                        userId = userId
                    )

                    call.respond(
                        HttpStatusCode.Created,
                        book
                    )
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

                    val book = bookService.findById(id)

                    if (book == null) {
                        call.respond(
                            HttpStatusCode.NotFound,
                            "Livro não encontrado"
                        )
                        return@get
                    }

                    call.respond(book)
                }
            }
        }


        post("/users") {

            val request = call.receive<UserCreateRequest>()

            val user = userService.create(request)

            call.respond(
                HttpStatusCode.Created,
                user
            )
        }
    }
}