package com.valar.barter.model.user

import java.util.*

data class UserDTO(
    val id: Long,
    val email: String,
    val createdAt: Date,
    val username: String,
    val firstName: String,
    val lastName: String,
    val confirmed: Boolean,
    val confirmationSent: Boolean,
    val phone: String? = null
)
