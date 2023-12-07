package com.example.printempsdemarrage.controller

import com.example.printempsdemarrage.dto.PanierDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class PanierController(val panierRepository: panierRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Create panier")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Panier created",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = PanierDTO::class)
                )]),
        ApiResponse(responseCode = "409", description = "Panier already exist",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/paniers")
    fun create(@RequestBody @Valid panier: PanierDTO): ResponseEntity<PanierDTO> =
            panierRepository.create(panier.asPanier()).fold(
                    { success ->
                        ResponseEntity.status(HttpStatus.CREATED).body(success.asPanierDTO()) },
                    { failure ->
                        ResponseEntity.status(HttpStatus.CONFLICT).build() })

    @Operation(summary = "List paniers")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List paniers",
                content = [Content(mediaType = "application/json",
                        array = ArraySchema(
                                schema = Schema(implementation = PanierDTO::class))
                )])])
    @GetMapping("/api/paniers")
    fun list() =
            panierRepository.list()
                    .map { it.asPanierDTO() }
                    .let {
                        ResponseEntity.ok(it)
                    }

    @Operation(summary = "Get panier by email of user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
                content = [
                    Content(mediaType = "application/json",
                            schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found")
    ])
    @GetMapping("/api/paniers/{email}")
    fun findOne(@PathVariable @Email email: String): ResponseEntity<PanierDTO> {
        val panier = panierRepository.get(email)
        return if (panier != null) {
            ResponseEntity.ok(panier.asPanierDTO())
        } else {
            throw PanierNotFoundError(email)
        }
    }

    @Operation(summary = "Update a panier by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Panier updated",
                content = [Content(mediaType = "application/json",
                        schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PutMapping("/api/paniers/{email}")
    fun update(@PathVariable @Email email: String, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> =
            if (email != panier.user.email) {
                ResponseEntity.badRequest().body("Invalid email")
            } else {
                panierRepository.update(panier.asPanier()).fold(
                        { success -> ResponseEntity.ok(success.asPanierDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }

    @Operation(summary = "Delete panier by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Panier deleted"),
        ApiResponse(responseCode = "400", description = "Panier not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("/api/paniers/{email}")
    fun delete(@PathVariable @Email email: String): ResponseEntity<Any> {
        val deleted = panierRepository.delete(email)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("Panier not found")
        } else {
            ResponseEntity.noContent().build()
        }
    }



    @Operation(summary = "Delete article from panier by user email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Article deleted"),
        ApiResponse(responseCode = "400", description = "Article not found",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("/api/paniers/{email}/{id}")
    fun delete(@PathVariable @Email email: String, id : String): ResponseEntity<Any> {
        val deleted = panierRepository.delete(email,id)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("Article not found")
        } else {
            ResponseEntity.noContent().build()
        }
    }
}
