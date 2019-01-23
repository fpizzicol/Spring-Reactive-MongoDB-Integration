package com.example.sample.routers

import com.example.sample.dto.BooksHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
class Router {

    @Bean
    fun homeRouter(handler: DefaultHandler) = router {
        (
                "/" and accept(MediaType.APPLICATION_JSON)).nest {
            GET("/", handler::home)
        }
    }
    @Bean
    fun booksRouter(handler: BooksHandler) = router {
        (
                "/books" and accept(MediaType.APPLICATION_JSON)).nest {

            GET("/", handler::getAll)
            GET("/{title}", handler::getBook)
        }
    }
}


@Component
class DefaultHandler {
    fun home(request: ServerRequest): Mono<ServerResponse> {
        return ok().build()
    }
}