package com.technzone.minibursa.utils.twilio.chat.channels

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.minibursa.common.MyApplication
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.api.response.Result
import com.technzone.minibursa.utils.twilio.chat.client.ChatClientManager
import com.technzone.minibursa.utils.twilio.chat.client.CustomChannelComparator
import com.technzone.minibursa.utils.twilio.chat.listeners.TaskCompletionListener
import com.twilio.chat.*
import com.twilio.chat.Channel.ChannelType
import com.twilio.chat.ChatClient.ConnectionState
import java.util.*

class ChannelManager private constructor() : ChatClientListener {
    var selectedChannel: Channel? = null
    private val chatClientManager: ChatClientManager
    lateinit var channelExtractor: ChannelExtractor
    private var channels: MutableList<Channel>? = null
    private var channelsObject: Channels? = null
    private var listener: ChatClientListener?
    lateinit var defaultChannelName: String
    lateinit var defaultChannelUniqueName: String
    private val handler: Handler
    private var isRefreshingChannels = false
    private val runnable: Runnable? = null
    private var newToken = ""
    fun getChannels(): List<Channel>? {
        return channels
    }

    fun leaveChannelWithHandler(
        channel: Channel,
        handler: StatusListener?
    ) {
        channel.leave(handler)
    }

    fun deleteChannelWithHandler(
        channel: Channel,
        handler: StatusListener?
    ) {
        channel.destroy(handler)
    }

    fun populateChannels(listener: LoadChannelListener) {
        handler.post {
            channelsObject = chatClientManager.getChatClient()!!.channels
            channels = ArrayList()
            chatClientManager.setClientListener(this@ChannelManager)
            listener.onChannelsFinishedLoading(channels)
        }
    }

    private fun extractChannelsFromPaginatorAndPopulate(
        channelsPaginator: Paginator<ChannelDescriptor>,
        listener: LoadChannelListener
    ) {
        channels = mutableListOf()
        channels?.clear()
        channelExtractor.extractAndSortFromChannelDescriptor(channelsPaginator,
            object :
                TaskCompletionListener<List<Channel>, String> {
                override fun onSuccess(channels: List<Channel>) {
                    this@ChannelManager.channels?.addAll(channels)
                    Collections.sort<Channel>(
                        this@ChannelManager.channels as List<Channel>,
                        CustomChannelComparator(defaultChannelName)
                    )
                    isRefreshingChannels = false
                    chatClientManager.setClientListener(this@ChannelManager)
                    listener.onChannelsFinishedLoading(this@ChannelManager.channels)
                }

                override fun onError(errorText: String) {
                    println("Error populating channels: $errorText")
                }
            })
    }

    fun createChannelWithName(
        name: String?,
        handler: StatusListener
    ) {
        channelsObject
            ?.channelBuilder()
            ?.withFriendlyName(name)
            ?.withType(ChannelType.PUBLIC)
            ?.build(object : CallbackListener<Channel>() {
                override fun onSuccess(newChannel: Channel) {
                    handler.onSuccess()
                }

                override fun onError(errorInfo: ErrorInfo) {
                    handler.onError(errorInfo)
                }
            })
    }

    fun getChannel(
        channelHash: String?,
        listener: StatusListener
    ) {
        channelsObject!!.getChannel(
            channelHash,
            object : CallbackListener<Channel>() {
                override fun onSuccess(channel: Channel) {
                    selectedChannel = channel
                    println("channelStatus1===>" + selectedChannel!!.status.name)
                    if (channel.status != Channel.ChannelStatus.JOINED) {
                        joinChannel(listener)
                    } else {
                        object : CountDownTimer(2000, 2000) {
                            override fun onFinish() {
                                listener.onSuccess()
                            }

                            override fun onTick(millisUntilFinished: Long) {
                            }
                        }.start()
                    }
                }

                override fun onError(errorInfo: ErrorInfo?) {
                    super.onError(errorInfo)
                    listener.onError(errorInfo)

                }
            })
    }

    fun getAllChannels() = liveData {
        emit(APIResource.loading())
        channelsObject = chatClientManager.getChatClient()!!.channels
        channels = ArrayList()
        chatClientManager.setClientListener(this@ChannelManager)
        channelsObject?.subscribedChannels
        if (channelsObject?.subscribedChannels != null && channelsObject?.subscribedChannels?.size ?: 0 > 0) {
            kotlinx.coroutines.delay(2000)
            channelsObject?.subscribedChannels?.sortedByDescending {
                it.lastMessageDate
            }
            APIResource.success(channelsObject?.subscribedChannels, "", 0)
        }
        else
            emit(
                APIResource.error(
                    msgs = "", data = null, statusCode = -1,
                    ResponseSubErrorsCodeEnum.getResponseSubErrorsCodeEnumByValue(-1)
                )
            )
    }

    fun getAllChannel(
        channelsResults: MutableLiveData<Result<MutableList<Channel>>>
    ) {
        handler.post {
            channelsObject = chatClientManager.getChatClient()!!.channels
            channels = ArrayList()
            chatClientManager.setClientListener(this@ChannelManager)
            channelsObject?.subscribedChannels
            if (channelsObject?.subscribedChannels != null && channelsObject?.subscribedChannels?.size ?: 0 > 0)
                channelsResults.postValue(Result.Success(channelsObject?.subscribedChannels))
            else
                channelsResults.postValue(Result.CustomError(0, ""))
//            channelsObject?.getPublicChannelsList(object :
//                CallbackListener<Paginator<ChannelDescriptor>>() {
//                override fun onSuccess(p0: Paginator<ChannelDescriptor>?) {
//                    if (p0 != null && p0.items.size > 0)
//                        channelsResults.postValue(Result.Success(p0?.items))
//                    else
//                        channelsResults.postValue(Result.CustomError(0, ""))
//                }
//
//                override fun onError(errorInfo: ErrorInfo?) {
//                    super.onError(errorInfo)
//                    channelsResults.postValue(
//                        Result.CustomError(
//                            errorInfo?.code ?: 0,
//                            errorInfo?.message ?: ""
//                        )
//                    )
//                }
//            })
        }

    }

    fun printStatus(status: String) {
        println("channelStatus===>$status")
    }

    fun joinChannel(listener: StatusListener) {
        println("channelStatusbeforeJoin===>" + selectedChannel!!.status.name)
        selectedChannel!!.join(object : StatusListener() {
            override fun onSuccess() {
                listener.onSuccess()
            }

            override fun onError(errorInfo: ErrorInfo) {
                println("joinError ===>$errorInfo")
                if (errorInfo.toString().contains("exists")) {
                    listener.onSuccess()
                } else {
                    listener.onError(errorInfo)
                }
            }
        })
    }


    fun setChannelListener(listener: ChatClientListener?) {
        this.listener = listener
    }

    fun setNewToken(token: String) {
        newToken = token
    }


    override fun onChannelAdded(channel: Channel) {
        if (listener != null) {
            listener!!.onChannelAdded(channel)
        }
    }

    override fun onChannelUpdated(
        channel: Channel,
        updateReason: Channel.UpdateReason
    ) {
        if (listener != null) {
            listener!!.onChannelUpdated(channel, updateReason)
        }
    }

    override fun onChannelDeleted(channel: Channel) {
        if (listener != null) {
            listener!!.onChannelDeleted(channel)
        }
    }

    override fun onChannelSynchronizationChange(channel: Channel) {
        if (listener != null) {
            listener!!.onChannelSynchronizationChange(channel)
        }
    }

    override fun onError(errorInfo: ErrorInfo) {
        if (listener != null) {
            listener!!.onError(errorInfo)
        }
    }

    override fun onClientSynchronization(synchronizationStatus: ChatClient.SynchronizationStatus) {}
    override fun onChannelJoined(channel: Channel) {
        if (listener != null) {
            listener!!.onChannelJoined(channel)
        }
    }

    override fun onChannelInvited(channel: Channel) {}
    override fun onUserUpdated(
        user: User,
        updateReason: User.UpdateReason
    ) {
        if (listener != null) {
            listener!!.onUserUpdated(user, updateReason)
        }
    }

    override fun onUserSubscribed(user: User) {}
    override fun onUserUnsubscribed(user: User) {}
    override fun onNewMessageNotification(
        s: String,
        s1: String,
        l: Long
    ) {
    }

    override fun onAddedToChannelNotification(s: String) {}
    override fun onInvitedToChannelNotification(s: String) {}
    override fun onRemovedFromChannelNotification(s: String) {}
    override fun onNotificationSubscribed() {}
    override fun onNotificationFailed(errorInfo: ErrorInfo) {}
    override fun onConnectionStateChange(connectionState: ConnectionState) {}
    override fun onTokenExpired() {
        if (listener != null) {
            listener!!.onTokenExpired()
        }
    }

    override fun onTokenAboutToExpire() {
        chatClientManager.getChatClient()
            ?.updateToken(newToken, object : StatusListener() {
                override fun onSuccess() {
                    println("Token updated successfully")
                }
            })
    }

    private fun setupListenerHandler(): Handler {
        var looper: Looper?
        val handler: Handler
        if (Looper.myLooper().also { looper = it } != null) {
            handler = Handler(looper!!)
        } else if (Looper.getMainLooper().also { looper = it } != null) {
            handler = Handler(looper!!)
        } else {
            throw IllegalArgumentException("Channel Listener must have a Looper.")
        }
        return handler
    }

    companion object {
        val instance = ChannelManager()
    }

    init {
        chatClientManager =
            MyApplication.instance.chatClientManager
        listener = this
        handler = setupListenerHandler()
    }
}