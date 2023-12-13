package com.example.printempsdemarrage.dbconfig

import com.example.printempsdemarrage.jpa.JpaRepo
import com.example.printempsdemarrage.jpa.PanierDatabaseInMemoryRepository
import com.example.printempsdemarrage.jpa.PanierDatabaseRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class DatabasePanierConfig {

    @ConditionalOnProperty("dbStatus",
            havingValue = "inMemory",
            matchIfMissing = false)
    @Bean
    fun inMemory() = PanierDatabaseInMemoryRepository()



    @ConditionalOnProperty("dbStatus",
            havingValue = "indb",
            matchIfMissing = true)
    @Bean
    fun h2(jpa : JpaRepo) = PanierDatabaseRepository(jpa)
}