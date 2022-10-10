package com.valar.barter.service

import com.valar.barter.config.JwtUtils
import com.valar.barter.entity.user.ERole
import com.valar.barter.entity.user.Role
import com.valar.barter.entity.user.User
import com.valar.barter.entity.user.toDTO
import com.valar.barter.model.exception.UserExistsException
import com.valar.barter.model.user.JwtResponse
import com.valar.barter.model.user.SignInRequest
import com.valar.barter.model.user.SignUpRequest
import com.valar.barter.model.user.UserDTO
import com.valar.barter.repository.RoleRepository
import com.valar.barter.repository.UserRepository
import com.valar.barter.service.security.UserDetailsImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashSet

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val encoder: PasswordEncoder,
    private val jwtUtils: JwtUtils
) {

    fun authenticateUser(loginRequest: SignInRequest): JwtResponse {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)
        val userDetails = authentication.principal as UserDetailsImpl
        val roles = userDetails.authorities.stream().map { item: GrantedAuthority -> item.authority }
            .collect(Collectors.toList())
        return JwtResponse(
            accessToken = jwt,
            0L,
            userDetails.username,
            roles
        )
    }

    fun registerUser(signUpRequest: SignUpRequest): UserDTO {
        if (userRepository.existsByEmail(signUpRequest.email)) {
            throw UserExistsException()
        }

        val user = User(
            email = signUpRequest.email,
            hashedPassword = encoder.encode(signUpRequest.password),
            enabled = true,
            createdAt = Date(),
            firstName = signUpRequest.firstName,
            lastName = signUpRequest.lastName,
            username = signUpRequest.username
        )
        val roles: MutableSet<Role> = HashSet()
        val userRole: Role = roleRepository.findByRole(ERole.ROLE_USER)
        roles.add(userRole)
        user.roles = roles

        val savedUser = userRepository.save(user)
        userRepository.flush()
        return savedUser.toDTO()
    }

    fun getUser(): UserDTO {
        val loggedInUser = SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
        val user = userRepository.findById(loggedInUser.id)
        require(user.isPresent) { "User cant be found" }
        return user.get().toDTO()
    }
}
