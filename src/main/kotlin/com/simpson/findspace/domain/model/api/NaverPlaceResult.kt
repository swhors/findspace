package com.simpson.findspace.domain.model.api

data class NaverPlaceResult(var lastBuildDate: String,
                            var total: Int,
                            var start: Int,
                            val display: Int,
                            var items: ArrayList<Item> ) {
    data class Item(
            var title: String?,
            var link: String?,
            var description: String?,
            var telephone: String?,
            var address: String?,
            var roadAddress: String?,
            var mapx: Int,
            var mapy: Int)
    {
        constructor(builder: Builder) :
                this(builder.title, builder.link,
                        builder.description, builder.telephone,
                        builder.address, builder.roadAddress,
                        builder.mapx, builder.mapy)
        class Builder {
            var title: String ?= null
                private set
            var link: String ?= null
                private set
            var description: String ?= null
                private set
            var telephone: String ?= null
                private set
            var address: String ?= null
                private set
            var roadAddress: String ?= null
                private set
            var mapx: Int = 0
                private set
            var mapy: Int = 0
                private set
            fun title(title: String) = apply { this.title = title }
            fun link(link: String) = apply { this.link = link }
            fun description(description: String) = apply { this.description = description }
            fun telephone(telephone: String) = apply { this.telephone = telephone }
            fun address(address: String) = apply { this.address = address }
            fun roadAddress(roadAddress: String) = apply { this.roadAddress = roadAddress }
            fun mapx(mapx: Int) = apply { this.mapx = mapx }
            fun mapy(mapy: Int) = apply { this.mapy = mapy }
            fun build() = Item(this)
        }
    }

    override fun toString(): String {
        return "lastBuildDate:$lastBuildDate,total:$total,start:$start,display:$display,items:${items.size}"
    }
}
