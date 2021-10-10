package com.technzone.minibursa.utils.twilio.chat.client

import android.content.Context
import com.technzone.minibursa.utils.twilio.chat.listeners.TaskCompletionListener
import com.twilio.chat.CallbackListener
import com.twilio.chat.ChatClient
import com.twilio.chat.ErrorInfo

class ChatClientBuilder(private val context: Context) :
    CallbackListener<ChatClient>() {
    private var buildListener: TaskCompletionListener<ChatClient, String>? = null
    fun build(
        token: String?,
        listener: TaskCompletionListener<ChatClient, String>?
    ) {
        val props = ChatClient.Properties.Builder()
            .setRegion("us1")
            .setCommandTimeout(10000)
            .createProperties()
        buildListener = listener
        ChatClient.create(
            context.applicationContext,
            token!!,
            props,
            this
        )
    }

    override fun onSuccess(chatClient: ChatClient) {
        buildListener!!.onSuccess(chatClient)
    }

    override fun onError(errorInfo: ErrorInfo) {
        buildListener!!.onError(errorInfo.message)
    }

}