package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.ArticleInPanierDTO
import com.example.printempsdemarrage.dto.PanierDTO
import com.example.printempsdemarrage.dto.PanierRepoInterface
import com.example.printempsdemarrage.exception.PanierAlreadyExistsException
import com.example.printempsdemarrage.exception.PanierNotFoundException
import org.springframework.data.domain.Example
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Repository
class PanierDatabaseRepository(private val jpaRepo: JpaRepo) : PanierRepoInterface {


    override fun getPaniers(): List<PanierDTO>{
        return jpaRepo.findAll()
    }

    override fun getPanier(id: String): PanierDTO? {
        val panier = jpaRepo.findById(id)
        return if (panier.isPresent) panier.get() else throw PanierNotFoundException("Panier with id $id not found")
    }



    override fun addPanier(panier: PanierDTO): PanierDTO {
        if (jpaRepo.existsById(panier.id.toString())) {
            throw PanierAlreadyExistsException("Panier with id ${panier.id} already exists")
        }
        return jpaRepo.save(panier)
    }

    override fun updatePanier(id: String, panier: PanierDTO): PanierDTO {
        if (!jpaRepo.existsById(id)) {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
        return jpaRepo.save(panier)
    }


    override fun deletePanier(id: String) {
        if (!jpaRepo.existsById(id)) {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
        jpaRepo.deleteById(id)
    }

    override fun addToPanier(id: String, article: ArticleInPanierDTO): PanierDTO {
        val panier = jpaRepo.findById(id)
        if (panier.isPresent) {
            val panierDTO = panier.get()
            val index = panierDTO.articles.indexOf(article)
            if (index != -1) {
                val newArticle = ArticleInPanierDTO(article.id, panierDTO.articles[index].quantity + article.quantity)
                panierDTO.articles[index] = newArticle
            } else {
                panierDTO.articles.add(article)
            }
            return jpaRepo.save(panierDTO)
        } else {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
    }

    override fun removeFromPanier(id: String, article: ArticleInPanierDTO): PanierDTO {
        val panier = jpaRepo.findById(id)
        if (panier.isPresent) {
            val panierDTO = panier.get()
            val index = panierDTO.articles.indexOf(article)
            if (index != -1) {
                val newArticle = ArticleInPanierDTO(article.id, panierDTO.articles[index].quantity - article.quantity)
                if (newArticle.quantity <= 0) {
                    panierDTO.articles.removeAt(index)
                } else {
                    panierDTO.articles[index] = newArticle
                }
            }
            return jpaRepo.save(panierDTO)
        } else {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
    }

    override fun getArticles(id: String): List<ArticleInPanierDTO> {
        val panier = jpaRepo.findById(id)
        if (panier.isPresent) {
            return panier.get().articles
        } else {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
    }

}