package com.example.printempsdemarrage.dto

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "Panier")
data class PanierDTO (
        @Id
        var id : String,

        @NotBlank
        var userEmail: String,

        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "id")
        var articles: MutableList<ArticleInPanierDTO>
)