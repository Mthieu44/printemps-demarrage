package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.ArticleInPanierDTO
import com.example.printempsdemarrage.dto.PanierDTO
import com.example.printempsdemarrage.dto.PanierRepoInterface
import com.example.printempsdemarrage.exception.PanierAlreadyExistsException
import com.example.printempsdemarrage.exception.PanierNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Example
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

//@Repository
class PanierDatabaseInMemoryRepository : PanierRepoInterface {

    private val map = mutableMapOf<String, PanierDTO>()
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun getPaniers(): List<PanierDTO>{
            return map.values.toList()
    }

    override fun getPanier(id: String): PanierDTO? {
        return map[id]
    }



    override fun addPanier(panier: PanierDTO): PanierDTO {
        logger.debug("=====> debug")
        val previous = map.putIfAbsent(panier.id, panier)
        if (previous != null) {
            throw PanierAlreadyExistsException("Panier with id ${panier.id} already exists")
        } else {
            return panier
        }
    }

    override fun updatePanier(id: String, panier: PanierDTO): PanierDTO {
        logger.debug("=====> debug")
        val updated = map.replace(id, panier)
        if (updated != null) {
            throw PanierNotFoundException("Panier with id $id does not exist")
        } else {
            return panier
        }
    }


    override fun deletePanier(id: String) {
        map.remove(id)
    }

    override fun addToPanier(id: String, article: ArticleInPanierDTO): PanierDTO {

        val panier = map[id]
        if (panier != null) {
            val index = panier.articles.indexOf(article)
            if (index != -1) {
                val newArticle = ArticleInPanierDTO(article.id, panier.articles[index].quantity + article.quantity)
                panier.articles[index] = newArticle
            } else {
                panier.articles.add(article)
            }
            return panier
        } else {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
    }

    override fun removeFromPanier(id: String, article: ArticleInPanierDTO): PanierDTO {
        val panier = map[id]
        if (panier != null) {
            val index = panier.articles.indexOf(article)
            if (index != -1) {
                val newArticle = ArticleInPanierDTO(article.id, panier.articles[index].quantity - article.quantity)
                if (newArticle.quantity <= 0) {
                    panier.articles.removeAt(index)
                } else {
                    panier.articles[index] = newArticle
                }
            }
            return panier
        } else {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
    }

    override fun getArticles(id: String): List<ArticleInPanierDTO> {
        val panier = map[id]
        if (panier != null) {
            return panier.articles
        } else {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
    }

}