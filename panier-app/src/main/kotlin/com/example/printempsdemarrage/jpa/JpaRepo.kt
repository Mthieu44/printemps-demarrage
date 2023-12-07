package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.PanierDTO
import org.springframework.data.jpa.repository.JpaRepository
interface JpaRepo : JpaRepository<PanierDTO, String>