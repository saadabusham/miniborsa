package com.technzone.flyus_owner.data.models.twilio.chat

import com.twilio.chat.Channel
import com.twilio.chat.Message

data class TwilioMessage(
    val channel: Channel?,
    val lastMessage: Message?
)