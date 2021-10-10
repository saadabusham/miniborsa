package com.technzone.minibursa.utils.twilio.chat.models

import java.io.InputStream

data class Media(val name: String, val type: String, val stream: InputStream)
