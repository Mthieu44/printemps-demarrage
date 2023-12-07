package com.example.printempsdemarrage.exception

class ArticleNotFoundException(message: String) : Exception(message)

class ArticleAlreadyExistsException(message: String) : Exception(message)