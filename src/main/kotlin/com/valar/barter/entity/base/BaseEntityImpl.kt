package com.valar.barter.entity.base

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@MappedSuperclass
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
abstract class BaseEntityImpl(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Version
    override var version: Long? = null,

    @JsonIgnore
    @Transient
    var allowCustomId: Boolean = false

) : BaseEntity, Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntityImpl) return false

        if (id != other.id) return false
        if (version != other.version) return false
        if (allowCustomId != other.allowCustomId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (version?.hashCode() ?: 0)
        result = 31 * result + allowCustomId.hashCode()
        return result
    }

    override fun toString(): String =
        "BaseEntityImpl(id=$id, version=$version, isAllowCustomId=$allowCustomId)"
}
