package com.example.printempsdemarrage.controller

import com.example.printempsdemarrage.dto.ArticleDTO
import com.example.printempsdemarrage.jpa.ArticleDatabaseRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.*

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
    @Tag(name = "Administration")
    @PostMapping("/api/articles")
    fun create(@RequestBody @Valid article: ArticleDTO): ResponseEntity<ArticleDTO> {
        logger.info("Création de l'article $article")
        return ResponseEntity.ok(articleRepository.addArticle(article))
    }


    @Operation(summary = "List articles")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List articles",
            content = [Content(mediaType = "application/json",
                array = ArraySchema(
                    schema = Schema(implementation = ArticleDTO::class))
            )])])
    @Tag(name = "Administration")
    @GetMapping("/api/articles")
    fun getAll(): ResponseEntity<List<ArticleDTO>> {
        logger.info("Mostrar todos los artículos")
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
    @Tag(name = "Administration")
    @GetMapping("/api/articles/{id}")
    fun findOne(@PathVariable id: String): ResponseEntity<ArticleDTO> {
        logger.info("Affichage de l'article d'ID : $id")
        return ResponseEntity.ok(articleRepository.getArticle(id))
    }

    @Operation(summary = "Update a article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = ArticleDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]),
        ApiResponse(responseCode = "409", description = "Article stock can't be negative",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @Tag(name = "Administration")
    @PutMapping("/api/articles/{id}")
    fun update(@PathVariable id: String, @RequestBody @Valid article: ArticleDTO): ResponseEntity<Any> {
        logger.info("Mise à jour de l'article d'ID : $id par l'aricle : $article")
        return ResponseEntity.ok(articleRepository.updateArticle(id, article))
    }

    @Operation(summary = "Delete article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article deleted"),
        ApiResponse(responseCode = "404", description = "Article not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @Tag(name = "Administration")
    @DeleteMapping("/api/articles/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        logger.info("Suppression de l'article d'ID : $id")
        articleRepository.deleteArticle(id)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Remove stock article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article stock updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = ArticleDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]),
        ApiResponse(responseCode = "409", description = "Article stock can't be negative",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @Tag(name = "Métier")
    @PutMapping("/api/articles/{id}/stock/remove/{quantity}")
    fun removeStock(@PathVariable id: String, @PathVariable quantity: Int): ResponseEntity<Any> {
        logger.info("Suppression d'une quantité de $quantity au stock de l'article d'ID : $id")
        val article = articleRepository.getArticle(id)
        val art = ArticleDTO(article.id, article.name, article.price, article.quantity - quantity, article.lastUpdate)
        return ResponseEntity.ok(articleRepository.updateArticle(id, art))
    }

    @Operation(summary = "Add stock article by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article stock updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = ArticleDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))]),
        ApiResponse(responseCode = "409", description = "Article stock can't be negative",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @Tag(name = "Métier")
    @PutMapping("/api/articles/{id}/stock/add/{quantity}")
    fun addStock(@PathVariable id: String, @PathVariable quantity: Int): ResponseEntity<Any> {
        logger.info("Ajout d'une quantité de $quantity au stock de l'article d'ID : $id")
        val article = articleRepository.getArticle(id)
        val art = ArticleDTO(article.id, article.name, article.price, article.quantity + quantity, article.lastUpdate)
        return ResponseEntity.ok(articleRepository.updateArticle(id, art))
    }
}
