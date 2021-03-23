package com.simpson.findspace.domain.model.local

data class PlaceResult(val place: String?) {
    constructor(builder: Builder) : this(builder.place)

    class Builder () {
        var place : String ?= null
            private set
        fun place(place: String?) = apply {this.place = place}
        fun build() = PlaceResult(this)
    }
}
