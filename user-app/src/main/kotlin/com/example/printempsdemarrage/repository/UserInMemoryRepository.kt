package com.example.printempsdemarrage.repository

import com.example.printempsdemarrage.dto.UserDTO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class UserInMemoryRepository : UserRepository {

    private val map = mutableMapOf<String, UserDTO>()

    override fun create(user: UserDTO): Result<UserDTO> {
        val previous = map.putIfAbsent(user.email, user)
        return if (previous == null) {
            Result.success(user)
        } else {
            Result.failure(Exception("User already exit"))
        }
    }

    override fun list() = map.values.toList()

    override fun get(email: String) = map[email]

    override fun update(user: UserDTO): Result<UserDTO> {
        val updated = map.replace(user.email, user)
        return if (updated == null) {
            Result.failure(Exception("User doesn't exit"))
        } else {
            Result.success(user)
        }
    }

    override fun delete(email: String) = map.remove(email)
}