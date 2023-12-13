package com.example.printempsdemarrage.entity

import com.example.printempsdemarrage.dto.ArticlePanierDTO
import com.example.printempsdemarrage.dto.UserPanierDTO
import jakarta.persistence.*

@Entity
@Table(name = "Panier")
data class PanierDTO (
        @Id
        var id : Int,

        @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
        var user: UserPanierDTO,

        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "panier")
        var articles: List<ArticlePanierDTO> = emptyList()
)