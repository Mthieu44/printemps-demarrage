package com.example.printempsdemarrage.dto

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.util.*

@Entity
@Table(name = "Panier")
data class PanierDTO (
        @Id
        var id : Int
        /*
        @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
        var user: UserDTO,
        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "panier")
        var articles: List<ArticleDTO> = emptyList()
         */
)