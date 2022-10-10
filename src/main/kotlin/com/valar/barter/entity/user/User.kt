package com.valar.barter.entity.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.valar.barter.model.user.UserDTO
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(unique = true)
    var username: String,
    @Column(unique = true)
    var email: String,
    @Column(name = "password")
    var hashedPassword: String,
    var enabled: Boolean = true,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var deletedAt: Date? = null,
    var firstName: String,
    var lastName: String,
    var confirmed: Boolean = false,
    @JsonIgnore
    var confirmationSent: Boolean = false,
    var phone: String? = null,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = HashSet<Role>()
) {

    val fullName: String get() = "$firstName $lastName"

    fun addRole(role: Role): Boolean = roles.add(role)

    fun removeRole(role: Role): Boolean = roles.remove(role)

    override fun toString(): String {
        return "User{" +
            "email='" + this.email + '\''.toString() +
            ", enabled=" + enabled +
            ", deletedAt=" + this.deletedAt +
            ", firstName='" + this.firstName + '\''.toString() +
            ", lastName='" + this.lastName + '\''.toString() +
            ", confirmed=" + this.confirmed +
            ", phone='" + this.phone + '\''.toString() +
            ", roles=" + roles +
            '}'.toString()
    }

    fun isAdmin(): Boolean {
        return roles.any { it.role == ERole.ROLE_ADMIN }
    }
}

fun User.toDTO() = UserDTO(
    id = id,
    username = username,
    email = email,
    createdAt = createdAt!!,
    firstName = firstName,
    lastName = lastName,
    confirmed = confirmed,
    confirmationSent = confirmationSent,
    phone = phone
)
