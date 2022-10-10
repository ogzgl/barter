package com.valar.barter.entity.base

import java.util.*
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@MappedSuperclass
abstract class TimestampableEntity(
    open var created: Date? = null,
    open var updated: Date? = null
) : BaseEntityImpl() {

    @PrePersist
    protected open fun onCreate() {
        created = if (created == null) Date() else created
    }

    @PreUpdate
    protected open fun onUpdate() {
        updated = Date()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TimestampableEntity) return false
        if (!super.equals(other)) return false

        if (created != other.created) return false
        if (updated != other.updated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (created?.hashCode() ?: 0)
        result = 31 * result + (updated?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "TimestampableEntity(created=$created, updated=$updated,super=${super.toString()})"
}
