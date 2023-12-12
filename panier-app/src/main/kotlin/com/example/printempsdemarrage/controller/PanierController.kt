package com.example.printempsdemarrage.controller

import com.example.printempsdemarrage.dto.PanierDTO
import com.example.printempsdemarrage.jpa.PanierDatabaseRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.persistence.Id
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class PanierController(val panierRepository: PanierDatabaseRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)


    @Operation(summary = "Create panier")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "panier created",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = PanierDTO::class)
            )]),
        ApiResponse(responseCode = "409", description = "panier already exist",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/paniers")
    fun create(@RequestBody @Valid panier: PanierDTO): ResponseEntity<PanierDTO> {
        return ResponseEntity.ok(panierRepository.addPanier(panier))
    }


    @Operation(summary = "List paniers")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List paniers",
            content = [Content(mediaType = "application/json",
                array = ArraySchema(
                    schema = Schema(implementation = PanierDTO::class))
            )])])
    @GetMapping("/api/paniers")
    fun getAll(): ResponseEntity<List<PanierDTO>> {
        return ResponseEntity.ok(panierRepository.getPaniers())
    }

    /*
    @Operation(summary = "Get panier by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The panier",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "404", description = "panier not found")
    ])
    @GetMapping("/api/paniers/{email}")
    fun findOne(@PathVariable @Email email: String): ResponseEntity<PanierDTO> {
        return ResponseEntity.ok(panierRepository.getPaniers(email))
    }


    @Operation(summary = "Update a panier by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "panier updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = PanierDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PutMapping("/api/paniers/{email}")
    fun update(@PathVariable @Email email: String, @RequestBody @Valid panier: PanierDTO): ResponseEntity<Any> {
        return ResponseEntity.ok(panierRepository.updatePanier(email, panier))
    }
     */

    @Operation(summary = "Delete panier by id")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Panier deleted"),
        ApiResponse(responseCode = "404", description = "Panier not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("/api/paniers/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        panierRepository.deletePanier(id)
        return ResponseEntity.ok().build()
    }
}
