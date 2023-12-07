package com.example.printempsdemarrage.jpa

import com.example.printempsdemarrage.dto.ArticleDTO
import org.springframework.data.jpa.repository.JpaRepository
interface JpaRepo : JpaRepository<ArticleDTO, String>