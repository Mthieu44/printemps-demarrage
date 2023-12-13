package com.example.printempsdemarrage.dto

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "Panier")
data class PanierDTO (
        @Id
        var id : Int,

        @NotBlank
        var user: String,

        var articles: MutableList<Pair<Int, Int>>
)