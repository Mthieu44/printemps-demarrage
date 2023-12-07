package com.example.printempsdemarrage.exception

class UserNotFoundException(message: String) : Exception(message)

class UserAlreadyExistsException(message: String) : Exception(message)