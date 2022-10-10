package com.valar.barter.model.exception

data class UserExistsException(override val message: String = "User exists with given email") : Exception(message)
