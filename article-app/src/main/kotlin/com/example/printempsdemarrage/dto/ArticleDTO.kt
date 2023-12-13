package com.example.printempsdemarrage.dto

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "Articles")
data class ArticleDTO (
        @field:Id
        val id: Int = 0,
        @field:NotBlank
        val name: String = "",
        @field:Min(0)
        val price: Double = 0.0,
        @field:Min(0)
        val quantity: Int = 0,
        val lastUpdate: LocalDate = LocalDate.now(),
        /*val panier: PanierDTO*/
)