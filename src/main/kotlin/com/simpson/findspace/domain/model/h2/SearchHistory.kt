package com.simpson.findspace.domain.model.h2

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class SearchHistory(
    @Id @GeneratedValue
    var id: Long? = null,

    var userName: String,
    var keyword: String,

    @CreationTimestamp
    var createDt: LocalDateTime = LocalDateTime.now()) {
    constructor(userName: String?, keyword: String?)
        : this(id = null, userName = userName !!,
               keyword = keyword !!, createDt = LocalDateTime.now())
}