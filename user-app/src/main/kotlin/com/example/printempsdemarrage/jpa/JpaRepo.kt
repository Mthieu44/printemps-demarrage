package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.UserDTO
import org.springframework.data.jpa.repository.JpaRepository
interface JpaRepo : JpaRepository<UserDTO, String>