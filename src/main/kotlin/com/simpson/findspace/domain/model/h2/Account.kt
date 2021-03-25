package com.simpson.findspace.domain.model.h2

import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.persistence.*

@Entity
data class Account(
    @Id @GeneratedValue
    var id: Long? = null,

    var userName: String?,
    var password: String?,

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    var roles: MutableSet<AccountRole>?,

    @CreationTimestamp
    var createDt: LocalDateTime = LocalDateTime.now()
){
    constructor(id: Long, userName: String?,
                password: String?, createDt: LocalDateTime?):
            this(id = id, userName = userName,
                    password = password, roles = mutableSetOf(),
                    createDt = createDt!!)
    
    constructor(builder: Builder) : this(null,
                                         builder.userName,
                                         builder.password,
                                         builder.roles,
                                         LocalDateTime.now())

    fun id(id: Long?) = apply { this.id = id }
    fun userName(userName: String?) = apply { this.userName = userName }
    fun password(password: String?) = apply { this.password = password }

    fun getAuthorities(): User {
        return User(
            this.userName, this.password,
            this.roles!!.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
        )
    }

    class Builder {
        var userName: String? = null
            private set
        var password: String? = null
            private set
        var roles: MutableSet<AccountRole>? = null
            private set

        fun userName(userName: String?) = apply { this.userName = userName }
        fun password(password: String?) = apply { this.password = password }
        fun roles(role: AccountRole?) = apply {
            if (role != null) {
                this.roles = setOf(role) as MutableSet<AccountRole>
            }
        }
        fun build() = Account(this)
    }
}