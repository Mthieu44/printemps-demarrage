package com.example.printempsdemarrage.controller

import com.example.printempsdemarrage.dto.UserDTO
import com.example.printempsdemarrage.exception.UserAlreadyExistsException
import com.example.printempsdemarrage.exception.UserNotFoundException
import com.example.printempsdemarrage.jpa.UserDatabaseRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserControllerTest {

    @MockkBean
    private lateinit var userDatabaseRepository: UserDatabaseRepository

    @Autowired
    private lateinit var userController: UserController

    @Test
    fun testCreateUser() {
        val user = UserDTO("cc@cc.cc", "cc", "rue du cc", false)
        every { userDatabaseRepository.addUser(user) } returns user

        val result = userController.create(user)
        assertEquals(user, result.body)
    }

    @Test
    fun testCreateUserAlreadyExists() {
        val user = UserDTO("cc@cc.cc", "cc", "rue du cc", false)
        every { userDatabaseRepository.addUser(user) } throws UserAlreadyExistsException("User with email ${user.email} already exists")

        val result = userController.create(user)
        assertEquals(400, result.statusCodeValue)
    }

    @Test
    fun testGetAll() {
        every { userDatabaseRepository.getUsers() } returns listOf(
            UserDTO("cc@cc.cc", "cc", "rue du cc", false),
            UserDTO("dd@dd.dd", "dd", "rue du dd", false)
        )
        val result = userController.getAll()
        assertEquals(2, result.body?.size)
    }

    @Test
    fun testFindOne() {
        val email = "cc@cc.cc"
        every { userDatabaseRepository.getUser(email) } returns UserDTO("cc@cc.cc", "cc", "rue du cc", false)
        val result = userController.findOne(email)
        assertEquals(UserDTO("cc@cc.cc", "cc", "rue du cc", false), result.body)
    }

    @Test
    fun testFindOneNotFound() {
        val email = "dd@dd.dd"
        every { userDatabaseRepository.getUser(email) } throws UserNotFoundException("User with email $email not found")
        val result = userController.findOne(email)
        assertEquals(404, result.statusCodeValue)
    }

    @Test
    fun testUpdate() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun getUserRepository() {
    }
}