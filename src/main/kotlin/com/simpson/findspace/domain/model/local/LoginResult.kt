package com.simpson.findspace.domain.model.local

data class LoginResult(val id: Long?, val token: String?) {
    constructor(builder: Builder): this(builder.id, builder.token )
 
    class Builder {
        var id : Long ?= null
            set
        var token: String ?= null
            set
        fun id(id: Long?) = apply { this.id = id }
        fun token(token: String) = apply {this.token = token}

        fun build() = LoginResult(this)
    }
}
