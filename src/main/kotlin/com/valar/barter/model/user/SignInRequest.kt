package com.valar.barter.model.user

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class SignInRequest(
    @NotBlank
    @NotEmpty
    val username: String,
    @NotBlank
    @NotEmpty
    val password: String
)
