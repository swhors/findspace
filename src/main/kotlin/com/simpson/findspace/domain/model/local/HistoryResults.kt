package com.simpson.findspace.domain.model.local

data class HistoryResults(val length: Int?,
                          val histories: ArrayList<HistoryResult>?) {
    constructor(builder: Builder) : this (builder.length, builder.histories)

    class Builder {
        var length: Int ?= null
        var histories: ArrayList<HistoryResult> ?= null

        fun length(length: Int?) = apply { this.length = length }
        fun histories(histories: ArrayList<HistoryResult>) = apply { this.histories = histories }

        fun build() = HistoryResults(this)
    }
}
