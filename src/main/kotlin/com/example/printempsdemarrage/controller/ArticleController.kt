package com.example.printempsdemarrage.controller


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController


@RestController
@Validated
class ArticleController(val articleRepository: ArticleRepository) {

    @Operation(summary = "Create Article")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Article created",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = ArticleDTO::class)
                )]),
        ApiResponse(responseCode = "409", description = "Article already exist",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/articles")
    fun create(@RequestBody @Valid article: ArticleDTO): ResponseEntity<ArticleDTO> =
            articleRepository.create(article.asArticle()).fold(
                    { success ->
                        ResponseEntity.status(HttpStatus.CREATED).body(success.asArticleDTO()) },
                    { failure ->
                        ResponseEntity.status(HttpStatus.CONFLICT).build() })

}