package com.technzone.minibursa.data.enums

enum class MessageType(val type:Int) {

    OUT_COMING_TEXT(1),
    OUT_COMING_IMAGE(2),
    INCOMING_TEXT(3),
    INCOMING_IMAGE(4)
}