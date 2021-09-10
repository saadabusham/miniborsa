package com.technzone.miniborsa.utils.twilio.chat.client

import com.twilio.chat.Channel
import java.util.*

class CustomChannelComparator internal constructor(val channelName:String) :
    Comparator<Channel> {
    private val defaultChannelName = "default name"
    override fun compare(lhs: Channel, rhs: Channel): Int {
        if (lhs.friendlyName.contentEquals(channelName)) {
            return -100
        } else if (rhs.friendlyName.contentEquals(channelName)) {
            return 100
        }
        return lhs.friendlyName.toLowerCase().compareTo(rhs.friendlyName.toLowerCase())
    }

}