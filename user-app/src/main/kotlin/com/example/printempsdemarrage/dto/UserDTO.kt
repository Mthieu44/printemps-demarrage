package com.example.printempsdemarrage.dto

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.util.Date

@Entity
@Table(name = "UserTable")
data class UserDTO (
    @Id @Email
    val email: String,
    @field:Size(min = 2, max = 20)
    val nom: String,
    @field:Size(min = 2, max = 80)
    val address: String,
    val subbed: Boolean,
    val lastCommand: Date,
)