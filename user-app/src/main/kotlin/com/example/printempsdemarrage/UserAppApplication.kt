package com.example.printempsdemarrage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserAppApplication

fun main(args: Array<String>) {
	runApplication<UserAppApplication>(*args)
}
