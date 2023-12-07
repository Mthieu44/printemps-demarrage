package com.example.printempsdemarrage.repository

import com.example.printempsdemarrage.dto.UserDTO


interface UserRepository {
    fun create(user: UserDTO): Result<UserDTO>
    fun list(): List<UserDTO>
    fun get(email: String): UserDTO?
    fun update(user: UserDTO): Result<UserDTO>
    fun delete(email: String): UserDTO?
}