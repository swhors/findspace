package com.simpson.findspace.domain.model.api.common

enum class Sort(sort: String) {
    SIM("sim"), DATE("date"), ASC("asc"), DSC("dsc");

    private var sortType: String = sort
}