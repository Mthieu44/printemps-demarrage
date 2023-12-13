package com.example.printempsdemarrage.dto

import com.example.printempsdemarrage.entity.PanierDTO
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.Date

@Entity
@Table(name = "UserTable")
data class UserPanierDTO (
    @field:NotBlank @Id
    val email: String = "",
    @field:Size(min = 2, max = 20)
    val nom: String = "",
    @field:Size(min = 2, max = 80)
    val address: String = "",
    val subbed: Boolean = false,
    val lastCommand: Date = Date(),

    @OneToOne
    val panier : PanierDTO?
)