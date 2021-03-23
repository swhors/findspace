package com.simpson.findspace.domain.model.local

data class FavoriteResult(val keyword: String?, val hitCount: Int?) {
    constructor(builder: Builder) : this(builder.keyword, builder.hitCount)

    class Builder {
        var keyword: String ?= null
            private set
        var hitCount: Int ?= null
            private set

        fun keyword(keyword: String) = apply { this.keyword = keyword }
        fun hitCount(hitCount: Int) = apply { this.hitCount = hitCount }

        fun build() = FavoriteResult(this)
    }
}
