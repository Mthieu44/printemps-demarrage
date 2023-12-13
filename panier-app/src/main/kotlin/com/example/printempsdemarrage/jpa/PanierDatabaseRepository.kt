package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.UserPanierDTO
import com.example.printempsdemarrage.entity.PanierDTO
import com.example.printempsdemarrage.exception.PanierAlreadyExistsException
import com.example.printempsdemarrage.exception.PanierNotFoundException
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Repository
class PanierDatabaseRepository(private val jpaRepo: JpaRepo) {

    private val userControllerUrl = "http://localhost:8080/api/users"
    fun getPaniers(): List<PanierDTO>{
        return jpaRepo.findAll()
    }



    fun addPanier(panier: PanierDTO): PanierDTO {
        val userEmail = panier.user.email
        try {
            val userResponse = RestTemplate().getForEntity("$userControllerUrl/$userEmail", String::class.java)
            if (userResponse.statusCode.isError) {
                throw HttpClientErrorException(userResponse.statusCode)
            }
        } catch (e: HttpClientErrorException.NotFound) {
            throw RuntimeException("User with Email $userEmail not found.")
        } catch (e: Exception) {
            throw RuntimeException("Error checking user existence: ${e.message}")
        }

        // Vérifier si l'utilisateur est déjà attribué à un panier
        if (getPaniers().any { it.user.email == panier.user.email }) {
            throw PanierAlreadyExistsException("User with Email $userEmail is already assigned to a panier.")
        }

        // Ajouter le panier
        if (jpaRepo.existsById(panier.id.toString())) {
            throw PanierAlreadyExistsException("Panier with id ${panier.id} already exists")
        }
        return jpaRepo.save(panier)
    }


    fun deletePanier(id: String) {
        if (!jpaRepo.existsById(id)) {
            throw PanierNotFoundException("Panier with id $id does not exist")
        }
        jpaRepo.deleteById(id)
    }
}