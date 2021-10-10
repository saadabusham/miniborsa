package com.technzone.minibursa.data.enums


enum class BusinessStatusEnums(val value: Int) {
    D(0),
    DRAFT(1),
    NEW(2),
    APPROVED(3),
    REJECTED(4),
    REMOVED(5)
}