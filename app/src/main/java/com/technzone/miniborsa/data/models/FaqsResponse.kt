package com.technzone.miniborsa.data.models

data class FaqsResponse(
        var id: Int,
        var question: String,
        var answer: String,

        @Transient
        var isExpanded: Boolean = false
)