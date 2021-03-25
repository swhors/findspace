package com.simpson.findspace.domain.model.local

data class HistoryResult(val keyword: String?, val created : String?) {
    constructor(builder: Builder) : this(builder.keyword, builder.created)

    class Builder {
        var keyword : String ?= null
            private set
        var created : String ?= null
            private set
        fun keyword(keyword: String?) = apply { this.keyword = keyword }
        fun created(created: String?) = apply { this.created = created }

        fun build() = HistoryResult(this)
    }
}
