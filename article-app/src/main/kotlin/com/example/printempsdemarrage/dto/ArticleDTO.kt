package com.example.printempsdemarrage.dto

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.util.*

@Entity
@Table(name = "Articles")
data class ArticleDTO (
        @field:Id
        val id: Int = 0,
        @field:NotBlank
        val name: String = "",
        val price: Double = 0.0,
        val quantity: Int = 0,
        val lastUpdate: Date = Date(),
        /*val panier: PanierDTO*/
)