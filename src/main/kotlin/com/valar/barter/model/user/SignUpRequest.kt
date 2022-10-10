package com.valar.barter.model.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignUpRequest(
    @NotBlank @Size(max = 50) @Email
    var username: String,

    @NotBlank @Size(max = 50) @Email
    var email: String,

    @NotBlank val firstName: String,

    @NotBlank val lastName: String,

    @NotBlank @Size(min = 6, max = 40)
    var password: String
)
