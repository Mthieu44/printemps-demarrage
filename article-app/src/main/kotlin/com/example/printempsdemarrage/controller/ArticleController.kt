package com.example.printempsdemarrage.controller

import com.example.printempsdemarrage.dto.ArticleDTO
import com.example.printempsdemarrage.jpa.ArticleDatabaseRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class ArticleController(val articleRepository: ArticleDatabaseRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Create article")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article created",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = ArticleDTO::class)
            )]),
        ApiResponse(responseCode = "409", description = "article already exist",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/articles")
    fun create(@RequestBody @Valid article: ArticleDTO): ResponseEntity<ArticleDTO> {
        return ResponseEntity.ok(articleRepository.addArticle(article))
    }


    @Operation(summary = "List articles")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List articles",
            content = [Content(mediaType = "application/json",
                array = ArraySchema(
                    schema = Schema(implementation = ArticleDTO::class))
            )])])
    @GetMapping("/api/articles")
    fun getAll(): ResponseEntity<List<ArticleDTO>> {
        return ResponseEntity.ok(articleRepository.getArticles())
    }


    @Operation(summary = "Get article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The article",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = ArticleDTO::class))]),
        ApiResponse(responseCode = "404", description = "Article not found")
    ])
    @GetMapping("/api/articles/{id}")
    fun findOne(@PathVariable id: String): ResponseEntity<ArticleDTO> {
        return ResponseEntity.ok(articleRepository.getArticle(id))
    }

    @Operation(summary = "Update a article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = ArticleDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PutMapping("/api/articles/{id}")
    fun update(@PathVariable id: String, @RequestBody @Valid article: ArticleDTO): ResponseEntity<Any> {
        return ResponseEntity.ok(articleRepository.updateArticle(id, article))
    }

    @Operation(summary = "Delete article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article deleted"),
        ApiResponse(responseCode = "404", description = "Article not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("/api/articles/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        articleRepository.deleteArticle(id)
        return ResponseEntity.ok().build()
    }
}
