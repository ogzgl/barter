package com.valar.barter.repository

import com.valar.barter.entity.user.ERole
import com.valar.barter.entity.user.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByRole(role: ERole): Role
}
