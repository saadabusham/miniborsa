package com.technzone.miniborsa.ui.core.messages.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.flyus_owner.data.models.twilio.chat.ChatChannel
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.data.repos.twilio.TwilioRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.extensions.getFullDate
import com.technzone.miniborsa.utils.extensions.getNotificationDateForamteed
import com.technzone.miniborsa.utils.extensions.toDate
import com.twilio.chat.CallbackListener
import com.twilio.chat.Channel
import com.twilio.chat.ErrorInfo
import com.twilio.chat.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clientRepo: TwilioRepo,
    private val userRepo: UserRepo
) : BaseViewModel() {

    lateinit var token: String
    val tokenResult: MutableLiveData<Result<String>> = MutableLiveData()

    fun getAccessToken() = liveData {
        emit(APIResource.loading())
        val response = clientRepo.getAccessToken()
        emit(response)
    }

    fun processChannels(channels: MutableList<Channel>): MutableList<ChatChannel> {
        val channelsProcessed = mutableListOf<ChatChannel>()
        channels.forEach {
            it?.messages?.getLastMessages(1, object : CallbackListener<List<Message>>() {
                override fun onSuccess(p0: List<Message>?) {
                    when (p0?.size ?: 0 > 0) {
                        true -> {
                            p0?.let { message ->
                                channelsProcessed.add(
                                    ChatChannel(
                                        it.sid,
                                        it.attributes.jsonObject?.optString(
                                            "fullName",
                                            context.resources.getString(R.string.investor)
                                        ) ?: "",
                                        it.attributes.jsonObject?.optString("picture", "") ?: "",
                                        getLastMessage(context, message?.get(0)),
                                        message?.get(0)?.dateCreated?.let {
                                            getDateFormated(
                                                it
                                            )
                                        } ?: ""
                                    )
                                )
                            }
                        }
                        else -> {
                            channelsProcessed.add(
                                getDefaultChannelData(context, it)
                            )
                        }
                    }
                }

                override fun onError(errorInfo: ErrorInfo?) {
                    super.onError(errorInfo)
                    channelsProcessed.add(
                        getDefaultChannelData(context, it)
                    )
                }
            })
        }
        return channelsProcessed
    }

    private fun getDefaultChannelData(context: Context, channel: Channel): ChatChannel {
        return ChatChannel(
            channel.sid,
            channel.attributes.jsonObject?.optString(
                "fullName",
                context.resources.getString(R.string.investor)
            ) ?: "",
            channel.attributes.jsonObject?.optString("picture", "") ?: "",

            context.resources.getString(
                R.string.chat_with_invistor
            ),
            channel.attributes.jsonObject?.optString("createdAt")?.let {
                getDateFormated(
                    it
                )
            } ?: ""
        )
    }

    private fun getLastMessage(context: Context, message: Message?): String {
        return if (message?.type == Message.Type.TEXT) message.messageBody
        else context.resources.getString(
            R.string.chat_with_invistor
        )
    }

    fun getDateFormated(date: String): String {
        return date?.getFullDate().toDate()?.time?.getNotificationDateForamteed()
            ?: ""
    }
}