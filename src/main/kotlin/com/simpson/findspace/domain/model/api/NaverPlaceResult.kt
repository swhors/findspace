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

    override fun toString(): String {
        return "lastBuildDate:$lastBuildDate,total:$total,start:$start,display:$display,items:${items.size}"
    }
}
