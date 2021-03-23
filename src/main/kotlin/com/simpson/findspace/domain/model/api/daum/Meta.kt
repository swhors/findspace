package com.simpson.findspace.domain.model.api.daum

data class Meta(var is_end: Boolean, var pageable_count: Int,
                var same_name: SameName, var total_count: Int) {
    data class SameName(var keyword: String,
                        var region: ArrayList<String>,
                        var selected_region: String)
}
