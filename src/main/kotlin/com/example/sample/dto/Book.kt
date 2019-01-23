package com.example.sample.dto

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Document(collection="books")
data class Book(@Id val id: String? = null,
                val title: String,
                val author: String)

@Component
class BooksHandler(private val repository: BookRepository) {
    fun getAll(request: ServerRequest): Mono<ServerResponse> {
        addBook()
        val interval = Flux.interval(Duration.ofSeconds(1))
        val books = repository.findAll()
        return ok().bodyToServerSentEvents(Flux.zip(interval, books).map { it.t2 })
    }

    fun getBook(request: ServerRequest): Mono<ServerResponse> {
        val title = request.pathVariable("title")
        val interval = Flux.interval(Duration.ofSeconds(1))
        val books = repository.findByTitle(title)
        return ok().bodyToServerSentEvents(Flux.zip(interval, books).map { it.t2 })
    }


    fun addBook(){
        val book = Book(title = "teste${Math.random()}", author = "Eu")
        val save = repository.save(book)
        save.subscribe()
    }
}


@Repository
interface BookRepository : ReactiveMongoRepository<Book, String> {
    fun findByTitle(name: String): Mono<Book>
}