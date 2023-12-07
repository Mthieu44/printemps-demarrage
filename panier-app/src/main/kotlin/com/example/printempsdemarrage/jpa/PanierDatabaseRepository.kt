package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.PanierDTO
import com.example.printempsdemarrage.exception.PanierAlreadyExistsException
import com.example.printempsdemarrage.exception.PanierNotFoundException
import org.springframework.stereotype.Repository

@Repository
class PanierDatabaseRepository(private val jpaRepo: JpaRepo) {
    fun getPaniers(): List<PanierDTO>{
        return jpaRepo.findAll()
    }



    fun addPanier(panier: PanierDTO): PanierDTO {
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