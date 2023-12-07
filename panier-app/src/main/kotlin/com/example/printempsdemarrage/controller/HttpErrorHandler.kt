package com.example.printempsdemarrage.controller

import com.example.printempsdemarrage.exception.PanierAlreadyExistsException
import com.example.printempsdemarrage.exception.PanierNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class HttpErrorHandler : ResponseEntityExceptionHandler() {
    override fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatusCode,
            request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body("cc je suis erreur not valid exception")
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
            ex: ConstraintViolationException,
            request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.badRequest().body("cc je suis erreur constraint violation exception")
    }

    @ExceptionHandler(PanierNotFoundException::class)
    fun handleUserNotFoundException(
            ex: PanierNotFoundException,
            request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(PanierAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(
            ex: PanierAlreadyExistsException,
            request: WebRequest
    ): ResponseEntity<Any> {
        return ResponseEntity.status(409).body("cc je suis erreur panier already exists exception")
    }

}