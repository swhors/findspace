package com.simpson.findspace.domain.model.local

data class FavoriteResults(val length: Int?,
                           val favorities: ArrayList<FavoriteResult>?) {
    constructor(builder : Builder) : this(builder.length, builder.favorities)

    class Builder() {
        var length: Int ?= null
        var favorities: ArrayList<FavoriteResult> ?= null

        fun length(length: Int?) = apply { this.length = length }
        fun favorities(favorities: ArrayList<FavoriteResult>?) = apply { this.favorities = favorities }

        fun build() = FavoriteResults(this)
    }
}
