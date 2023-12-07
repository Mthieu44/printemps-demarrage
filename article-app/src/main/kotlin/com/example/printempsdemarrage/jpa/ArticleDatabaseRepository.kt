package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.ArticleDTO
import com.example.printempsdemarrage.exception.ArticleAlreadyExistsException
import com.example.printempsdemarrage.exception.ArticleNotFoundException
import org.springframework.stereotype.Repository

@Repository
class ArticleDatabaseRepository(private val jpaRepo: JpaRepo) {
    fun getArticles(): List<ArticleDTO>{
        return jpaRepo.findAll()
    }

    fun getArticle(id: String): ArticleDTO {
        val article = jpaRepo.findById(id)
        return if (article.isPresent) article.get() else throw ArticleNotFoundException("User with email $id not found")
    }

    fun addArticle(article: ArticleDTO): ArticleDTO {
        if (jpaRepo.existsById(article.id.toString())) {
            throw ArticleAlreadyExistsException("User with email ${article.id} already exists")
        }
        return jpaRepo.save(article)
    }

    fun updateArticle(id: String, article: ArticleDTO): ArticleDTO {
        if (!jpaRepo.existsById(id)) {
            throw ArticleNotFoundException("User with email $id does not exist")
        }
        return jpaRepo.save(article)
    }

    fun deleteArticle(id: String) {
        if (!jpaRepo.existsById(id)) {
            throw ArticleNotFoundException("User with email $id does not exist")
        }
        jpaRepo.deleteById(id)
    }
}