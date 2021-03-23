package com.simpson.findspace.domain.model.api.daum

data class Document(var address_name: String,
                    var category_group_code: String,
                    var category_group_name: String,
                    var category_name: String,
                    var distance: String,
                    var id: String,
                    var phone: String,
                    var place_name: String,
                    var place_url: String,
                    var road_address_name: String,
                    var x: String, val y: String) {
}
