package com.simpson.findspace.domain.model.h2

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class SearchCache(
        @Id @GeneratedValue
        var id: Long? = null,

        var keyword: String,

        @Column(columnDefinition = "varchar2(1000)")
        var places: String,

        var hitcount: Int,

        @CreationTimestamp
        var created: LocalDateTime = LocalDateTime.now(),

        @UpdateTimestamp
        var updated: LocalDateTime = LocalDateTime.now() ) {
    
    constructor(keyword: String?, places: String?, hitcount: Int?)
            : this (id = null,
                    keyword = keyword !!,
                    places = places !!,
                    hitcount = hitcount !!,
                    created = LocalDateTime.now(),
                    updated = LocalDateTime.now())
}