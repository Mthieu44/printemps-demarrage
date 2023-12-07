package com.example.printempsdemarrage.controller

import com.example.printempsdemarrage.dto.UserDTO
import com.example.printempsdemarrage.jpa.UserDatabaseRepository
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
class UserController(val userRepository: UserDatabaseRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Create user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User created",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = UserDTO::class)
            )]),
        ApiResponse(responseCode = "409", description = "User already exist",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/users")
    fun create(@RequestBody @Valid user: UserDTO): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userRepository.addUser(user))
    }


    @Operation(summary = "List users")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List users",
            content = [Content(mediaType = "application/json",
                array = ArraySchema(
                    schema = Schema(implementation = UserDTO::class))
            )])])
    @GetMapping("/api/users")
    fun getAll(): ResponseEntity<List<UserDTO>> {
        return ResponseEntity.ok(userRepository.getUsers())
    }


    @Operation(summary = "Get user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The user",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class))]),
        ApiResponse(responseCode = "404", description = "User not found")
    ])
    @GetMapping("/api/users/{email}")
    fun findOne(@PathVariable @Email email: String): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userRepository.getUser(email))
    }

    @Operation(summary = "Update a user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = UserDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PutMapping("/api/users/{email}")
    fun update(@PathVariable @Email email: String, @RequestBody @Valid user: UserDTO): ResponseEntity<Any> {
        return ResponseEntity.ok(userRepository.updateUser(email, user))
    }

    @Operation(summary = "Delete user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User deleted"),
        ApiResponse(responseCode = "404", description = "User not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
    @DeleteMapping("/api/users/{email}")
    fun delete(@PathVariable @Email email: String): ResponseEntity<Any> {
        userRepository.deleteUser(email)
        return ResponseEntity.ok().build()
    }
}
