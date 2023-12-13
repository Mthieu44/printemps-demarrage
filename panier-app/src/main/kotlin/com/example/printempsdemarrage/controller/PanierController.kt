package com.example.printempsdemarrage.controller

import com.example.printempsdemarrage.dto.ArticleInPanierDTO
import com.example.printempsdemarrage.dto.PanierDTO
import com.example.printempsdemarrage.dto.PanierRepoInterface
import com.example.printempsdemarrage.exception.PanierAlreadyExistsException
import com.example.printempsdemarrage.jpa.PanierDatabaseRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@RestController
@Validated
class PanierController(val panierRepository: PanierRepoInterface) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val userControllerUrl = "http://localhost:8080/api/users"


    @Operation(summary = "Create panier")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "panier created",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = PanierDTO::class)
            )]),
        ApiResponse(responseCode = "409", description = "panier already exist",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @Tag(name = "Administration")
    @PostMapping("/api/paniers")
    fun create(@RequestBody @Valid panier: PanierDTO): ResponseEntity<PanierDTO> {
        val userEmail = panier.user
        try {
            val userResponse = RestTemplate().getForEntity("$userControllerUrl/$userEmail", String::class.java)
            if (userResponse.statusCode.isError) {
                throw HttpClientErrorException(userResponse.statusCode)
            }
        } catch (e: HttpClientErrorException.NotFound) {
            throw RuntimeException("User with Email $userEmail not found.")
        } catch (e: Exception) {
            throw RuntimeException("Error checking user existence: ${e.message}")
        }

        // Vérifier si l'utilisateur est déjà attribué à un panier
        if (panierRepository.getPaniers().any { it.user == panier.user }) {
            throw PanierAlreadyExistsException("User with Email $userEmail is already assigned to a panier.")
        }
        return ResponseEntity.ok(panierRepository.addPanier(panier))
    }


    @Operation(summary = "List paniers")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List paniers",
            content = [Content(mediaType = "application/json",
                array = ArraySchema(
                    schema = Schema(implementation = PanierDTO::class))
            )])])
    @Tag(name = "Administration")
    @GetMapping("/api/paniers")
    fun getAll(): ResponseEntity<List<PanierDTO>> {
        return ResponseEntity.ok(panierRepository.getPaniers())
    }

    @Operation(summary = "Get panier by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "panier not found")
    ])
    @Tag(name = "Administration")
    @GetMapping("/api/paniers/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<PanierDTO> {
        return ResponseEntity.ok(panierRepository.getPanier(id))
    }


    @Operation(summary = "Update a panier by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "panier updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @Tag(name = "Administration")
    @PutMapping("/api/paniers/{id}")
    fun update(@PathVariable id: String, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> {
        return ResponseEntity.ok(panierRepository.updatePanier(id, panier))
    }

    @Operation(summary = "Delete panier by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Panier deleted"),
        ApiResponse(responseCode = "404", description = "Panier not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @Tag(name = "Administration")
    @DeleteMapping("/api/paniers/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        panierRepository.deletePanier(id)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Add article to panier")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article added to panier",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @Tag(name = "Métier")
    @PostMapping("/api/paniers/{id}/article")
    fun addArticle(@PathVariable id: String, @RequestBody @Valid article: ArticleInPanierDTO): ResponseEntity<PanierDTO> {
        val articleResponse = RestTemplate().getForEntity("http://localhost:8081/api/articles/${article.id}/stock/remove/${article.quantity}", String::class.java)
        if (articleResponse.statusCode.isError) {
            throw HttpClientErrorException(articleResponse.statusCode)
        }
        return ResponseEntity.ok(panierRepository.addToPanier(id, article))
    }

    @Operation(summary = "Remove article from panier")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Article removed from panier",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "Panier not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @Tag(name = "Métier")
    @DeleteMapping("/api/paniers/{id}/article")
    fun removeArticle(@PathVariable id: String, @RequestBody @Valid article: ArticleInPanierDTO): ResponseEntity<PanierDTO> {
        val articleResponse = RestTemplate().getForEntity("http://localhost:8081/api/articles/${article.id}/stock/add/${article.quantity}", String::class.java)
        if (articleResponse.statusCode.isError) {
            throw HttpClientErrorException(articleResponse.statusCode)
        }
        return ResponseEntity.ok(panierRepository.removeFromPanier(id, article))
    }
}
