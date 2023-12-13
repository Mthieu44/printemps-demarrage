package com.example.printempsdemarrage.dto

interface PanierRepoInterface {
    fun getPaniers(): List<PanierDTO>
    fun getPanier(id: String): PanierDTO?
    fun addPanier(panier: PanierDTO) : PanierDTO
    fun updatePanier(id: String, panier: PanierDTO) : PanierDTO
    fun deletePanier(id: String)
    fun addToPanier(id: String, article: ArticleInPanierDTO): PanierDTO
    fun removeFromPanier(id: String, article: ArticleInPanierDTO): PanierDTO
    fun getArticles(id: String): List<ArticleInPanierDTO>
}