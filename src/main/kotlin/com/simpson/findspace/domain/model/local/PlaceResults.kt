package com.simpson.findspace.domain.model.local

data class PlaceResults(val keyword: String?,
                        val length: Int?,
                        val places: ArrayList<PlaceResult>?) {
    constructor(builder: Builder) : this(keyword = builder.keyword,
                                         length = builder.length,
                                         places = builder.places)

    class Builder () {
        var keyword: String ?= null
            private set
        var length: Int ?= null
            private set
        var places : ArrayList<PlaceResult> ?= null
            private set

        fun places(places: ArrayList<PlaceResult>?) = apply { this.places = places }
        fun keyword(keyword: String?) = apply { this.keyword = keyword }
        fun length(length: Int?) = apply { this.length = length }

        fun build() = PlaceResults(this)
    }
}
