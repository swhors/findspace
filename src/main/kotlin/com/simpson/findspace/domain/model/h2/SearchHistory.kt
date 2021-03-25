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
    
    // JPA의 Select를 위하여 기본 생성자를 추가 합니다.
    constructor() : this(null, "", "", LocalDateTime.now())
    
    class Builder() {
        private var userName: String ?= null
            set
        private var keyword: String ?= null
            set
        
        fun userName(userName: String) = apply {this.userName = userName}
        fun keyword(keyword: String) = apply {this.keyword = keyword}
        
        fun build() = SearchHistory(userName = this.userName, keyword = this.keyword)
    }
}