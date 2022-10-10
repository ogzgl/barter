package com.valar.barter.controller

import com.valar.barter.model.user.JwtResponse
import com.valar.barter.model.user.SignInRequest
import com.valar.barter.model.user.SignUpRequest
import com.valar.barter.model.user.UserDTO
import com.valar.barter.service.AuthService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/signin")
    fun authenticateUser(
        @Valid @RequestBody
        signInRequest: SignInRequest
    ): JwtResponse {
        return authService.authenticateUser(signInRequest)
    }

    @PostMapping("/signup")
    fun registerUser(
        @Valid @RequestBody
        signUpRequest: SignUpRequest
    ): UserDTO {
        return authService.registerUser(signUpRequest)
    }

    @GetMapping("/me")
    fun getUser(): UserDTO {
        return authService.getUser()
    }
}
