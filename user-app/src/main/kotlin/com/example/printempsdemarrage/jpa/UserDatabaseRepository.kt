package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.UserDTO
import com.example.printempsdemarrage.exception.UserAlreadyExistsException
import com.example.printempsdemarrage.exception.UserNotFoundException
import org.springframework.stereotype.Repository

@Repository
class UserDatabaseRepository(private val jpaRepo: JpaRepo) {
    fun getUsers(): List<UserDTO>{
        return jpaRepo.findAll()
    }

    fun getUser(email: String): UserDTO {
        val user = jpaRepo.findById(email)
        return if (user.isPresent) user.get() else throw UserNotFoundException("User with email $email not found")
    }

    fun addUser(person: UserDTO): UserDTO {
        if (jpaRepo.existsById(person.email)) {
            throw UserAlreadyExistsException("User with email ${person.email} already exists")
        }
        return jpaRepo.save(person)
    }

    fun updateUser(email: String, person: UserDTO): UserDTO {
        if (!jpaRepo.existsById(email)) {
            throw UserNotFoundException("User with email $email does not exist")
        }
        return jpaRepo.save(person)
    }

    fun deleteUser(email: String) {
        if (!jpaRepo.existsById(email)) {
            throw UserNotFoundException("User with email $email does not exist")
        }
        jpaRepo.deleteById(email)
    }
}