package com.technzone.miniborsa.utils.twilio.chat.client

import ToastStatusListener
import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.technzone.miniborsa.utils.twilio.chat.listeners.TaskCompletionListener
import com.twilio.chat.ChatClient
import com.twilio.chat.ChatClientListener

class ChatClientManager(private val context: Context) {
    private var chatClient: ChatClient? = null
    private val chatClientBuilder: ChatClientBuilder = ChatClientBuilder(context)
    fun setClientListener(listener: ChatClientListener?) {
        if (chatClient != null) {
            chatClient!!.addListener(listener!!)
        }
    }

    fun getChatClient(): ChatClient? {
        return chatClient
    }

    fun setupFcmToken() {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                return@OnCompleteListener
//            }
//            chatClient?.registerFCMToken(ChatClient.FCMToken(task.result ?: ""),
//                ToastStatusListener(
//                    "Firebase Messaging registration successful",
//                    "Firebase Messaging registration not successful"))
//        })

    }

    fun buildClient(
        token: String?,
        listener: TaskCompletionListener<Void?, String?>
    ) {
        chatClientBuilder.build(
            token,
            object : TaskCompletionListener<ChatClient, String> {
                override fun onSuccess(chatClient: ChatClient) {
                    this@ChatClientManager.chatClient = chatClient
                    setupFcmToken()
                    listener.onSuccess(null)
                }

                override fun onError(message: String) {
                    listener.onError(message)
                }
            })
    }
    fun shutdown() {
        if (chatClient != null) {
            chatClient!!.shutdown()
            chatClient = null
        }
    }

}