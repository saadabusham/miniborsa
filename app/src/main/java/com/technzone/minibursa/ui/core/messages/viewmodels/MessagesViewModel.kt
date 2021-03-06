package com.technzone.minibursa.ui.core.messages.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.minibursa.data.models.twilio.chat.ChatChannel
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.repos.twilio.TwilioRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.extensions.getFullDate
import com.technzone.minibursa.utils.extensions.getNotificationDateForamteed
import com.technzone.minibursa.utils.extensions.toDate
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

    fun getAccessToken(businessId: Int? = null) = liveData {
        emit(APIResource.loading())
        val response = clientRepo.getTwilioToken(Constants.Twilio.CHAT_PUSH_CREDENTIALS, businessId)
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

    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
    fun canViewMessages(): Boolean {
        return userRepo.getUser()?.roles?.filter { it.role == UserRoleEnums.INVESTOR_ROLE.value || it.role == UserRoleEnums.BUSINESS_ROLE.value }
            ?.isNotEmpty() == true
    }
}