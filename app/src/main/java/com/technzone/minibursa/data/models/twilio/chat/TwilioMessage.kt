package com.technzone.minibursa.data.models.twilio.chat

import com.twilio.chat.Channel
import com.twilio.chat.Message

data class TwilioMessage(
    val channel: Channel?,
    val lastMessage: Message?
)