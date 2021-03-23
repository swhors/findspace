package com.simpson.findspace.domain.model.api

import com.simpson.findspace.domain.model.api.daum.Document
import com.simpson.findspace.domain.model.api.daum.Meta

data class DaumPlaceResult(var documents: ArrayList<Document>,
                           var meta: Meta)
