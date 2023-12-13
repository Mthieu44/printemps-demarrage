package com.example.printempsdemarrage.dto

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "ArticleInPanier")
data class ArticleInPanierDTO (

        @Id var id: Int,
        var quantity: Int
)