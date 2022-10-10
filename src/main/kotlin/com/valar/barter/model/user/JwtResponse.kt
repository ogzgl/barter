package com.valar.barter.model.user

data class JwtResponse(
    var accessToken: String,
    var id: Long,
    var email: String,
    val roles: List<String>,
    val tokenType: String = "Bearer"
)
