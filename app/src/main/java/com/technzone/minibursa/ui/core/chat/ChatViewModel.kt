package com.technzone.minibursa.ui.core.chat

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.Result
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.repos.twilio.TwilioRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.twilio.chat.CallbackListener
import com.twilio.chat.ErrorInfo
import com.twilio.chat.Message
import com.twilio.chat.Messages
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val clientRepo: TwilioRepo,
    private val userRepo: UserRepo,
    @ApplicationContext context: Context
) : BaseViewModel() {
    val toolbarTitle: MutableLiveData<String> = MutableLiveData()

    init {
        toolbarTitle.postValue(context.resources.getString(R.string.app_name))
    }

    var last_loaded_message_index: Long = 0
    lateinit var token: String
    val tokenResult: MutableLiveData<Result<String>> = MutableLiveData()
    val messagesResult: MutableLiveData<Result<List<Message>>> = MutableLiveData()

    fun getAccessToken(businessId: Int? = null) = liveData {
        emit(APIResource.loading())
        val response = clientRepo.getTwilioToken(Constants.Twilio.CHAT_PUSH_CREDENTIALS, businessId)
        emit(response)
    }

    fun loadMessages(messagesObject: Messages?, loadMore: Boolean = false) {
        if (messagesObject != null) {
            messagesResult.postValue(Result.Loading)
            if (!loadMore) {
                messagesObject!!.getLastMessages(
                    15,
                    object : CallbackListener<List<Message>>() {
                        override fun onSuccess(messageList: List<Message>) {
                            if (messageList.isNotEmpty()) {
                                last_loaded_message_index =
                                    when (messageList[messageList.size - 1].messageIndex <= 14) {
                                        true -> 0
                                        false -> messageList[0].messageIndex
                                    }
                            }
                            if (!messageList.isNullOrEmpty()) {
                                messagesResult.postValue(Result.Success(messageList))
                            } else {
                                returnNoMoreData()
                            }
                        }

                        override fun onError(errorInfo: ErrorInfo) {
                            super.onError(errorInfo)
                            messagesResult.postValue(
                                Result.CustomError(
                                    errorCode = errorInfo.code,
                                    message = errorInfo.message
                                )
                            )
                        }
                    })
            } else {
                if (last_loaded_message_index != 0L) {
                    messagesObject!!.getMessagesBefore(last_loaded_message_index, 15,
                        object :
                            CallbackListener<List<Message>>() {
                            override fun onSuccess(messageList: List<Message>) {
                                last_loaded_message_index = messageList[0].messageIndex
                                if (!messageList.isNullOrEmpty()) {
                                    messagesResult.postValue(Result.Success(messageList))
                                } else {
                                    returnNoMoreData()
                                }
                            }

                            override fun onError(errorInfo: ErrorInfo) {
                                super.onError(errorInfo)
                                messagesResult.postValue(
                                    Result.CustomError(
                                        errorCode = errorInfo.code,
                                        message = errorInfo.message
                                    )
                                )
                            }
                        })
                } else {
                    // there is no more messages
                    returnNoMoreData()
                }
            }
        }
    }

    private fun returnNoMoreData() {
        messagesResult.postValue(
            Result.CustomError(
                errorCode = 0,
                message = "no more data"
            )
        )
    }

    fun getMyId(): String {
        return userRepo.getUser()?.id ?: ""
    }

//
//
//
//    fun isUserLoggedIn() = userRepo.getUserStatus() == UserEnums.UserState.LoggedIn
}