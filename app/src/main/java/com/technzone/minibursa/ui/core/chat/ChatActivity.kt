package com.technzone.minibursa.ui.core.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.paginate.Paginate
import com.technzone.minibursa.R
import com.technzone.minibursa.common.MyApplication
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.api.response.Result
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.databinding.ActivityChatBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.core.chat.adapters.MessageRecyclerAdapter
import com.technzone.minibursa.ui.core.chat.viewimage.ViewImageActivity
import com.technzone.minibursa.utils.HandleRequestFailedUtil
import com.technzone.minibursa.utils.ImagePickerUtil
import com.technzone.minibursa.utils.captureImage
import com.technzone.minibursa.utils.extensions.gone
import com.technzone.minibursa.utils.extensions.longToast
import com.technzone.minibursa.utils.extensions.showErrorAlert
import com.technzone.minibursa.utils.extensions.visible
import com.technzone.minibursa.utils.recycleviewutils.VerticalSpaceDecoration
import com.technzone.minibursa.utils.twilio.chat.channels.ChannelExtractor
import com.technzone.minibursa.utils.twilio.chat.channels.ChannelManager
import com.technzone.minibursa.utils.twilio.chat.channels.LoadChannelListener
import com.technzone.minibursa.utils.twilio.chat.client.ChatClientManager
import com.technzone.minibursa.utils.twilio.chat.listeners.TaskCompletionListener
import com.twilio.chat.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chat.*
import java.io.FileInputStream

@AndroidEntryPoint
class ChatActivity : BaseBindingActivity<ActivityChatBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener, ChatClientListener,
    ChannelListener {

    private var channelHash: String = "MiniBorsaCustomer"
    private lateinit var channelManager: ChannelManager
    private var currentChannel: Channel? = null
    private var messagesObject: Messages? = null
    private val viewModel: ChatViewModel by viewModels { defaultViewModelProviderFactory }
    lateinit var messageRecycleAdapter: MessageRecyclerAdapter
    private var chatClientManager: ChatClientManager? = null
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false
    private var refreshDataOnResume = true
    val toolbarTitle: MutableLiveData<String> = MutableLiveData()
    override fun onResume() {
        super.onResume()
        if (refreshDataOnResume)
            currentChannel?.let {
                clearData()
                loadData(it)
            }
        else
            refreshDataOnResume = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat, hasToolbar = false)
        setUpBinding()
        channelHash = intent.getStringExtra(Constants.Twilio.CHANNEL_ID) ?: ""
        chatClientManager = MyApplication.instance.chatClientManager
        viewModel.toolbarTitle.postValue(intent.getStringExtra(Constants.Twilio.USER_NAME))
        init()
        messagesResultObserver()
        setUpListeners()
        loadingObserver()
        twilioLogin()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
        binding?.layoutChatToolbar?.viewModel = viewModel
    }

    private fun init() {
        setUpMessageRecyclerView()
    }

    private fun setUpListeners() {
        binding?.cvSend?.setOnClickListener {
            if (!binding?.edMessage?.text.isNullOrEmpty()) {
                sendMessage()
            }
        }
        binding?.imgAttachment?.setOnClickListener {
            refreshDataOnResume = false
            captureImage(
                requestCode = ImagePickerUtil.TAKE_USER_IMAGE_REQUEST_CODE
            )
        }

        binding?.layoutChatToolbar?.imgBack?.setOnClickListener {
            finish()
        }
    }

    private fun sendMessage() {
        val messageOptions =
            Message.options().withBody(binding?.edMessage?.text.toString())
        messagesObject?.sendMessage(
            messageOptions,
            object : CallbackListener<Message>() {
                override fun onSuccess(p0: Message?) {
                }

                override fun onError(errorInfo: ErrorInfo?) {
                    super.onError(errorInfo)
                    longToast(errorInfo?.message)
                }
            })
        binding?.edMessage?.setText("")
    }

    private fun twilioLogin() {
        val businessId = if(intent.getIntExtra(Constants.BundleData.BUSINESS_ID,-1) != -1) intent.getIntExtra(Constants.BundleData.BUSINESS_ID,-1) else null
        viewModel.getAccessToken(businessId).observe(this, accessTokenResultObserver())
    }

    private fun accessTokenResultObserver(): CustomObserverResponse<String> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<String> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: String?
                ) {
                    viewModel.token = data ?: ""
                    checkTwilioClient()
                }
            })
    }

    private fun checkTwilioClient() {
        initializeClient()
//        if (chatClientManager!!.getChatClient() == null) {
//            initializeClient()
//        } else {
//            handleChannel()
//        }
    }

    private fun initializeClient() {
        chatClientManager?.buildClient(
            viewModel.token,
            object :
                TaskCompletionListener<Void?, String?> {
                override fun onSuccess(aVoid: Void?) {
                    println("===>client created")
                    handleChannel()
                }

                override fun onError(errorMessage: String?) {
                    binding?.layoutShimmer?.shimmerViewContainer?.gone()
                    showErrorAlert(
                        resources.getString(R.string.app_name),
                        "Failed tot create client"
                    )
                }
            })
    }

    fun handleChannel() {
        channelManager = ChannelManager.instance
        channelManager.channelExtractor =
            ChannelExtractor(
                channelHash
            )
        channelManager.setChannelListener(this)
        channelManager.defaultChannelName = channelHash
        channelManager.defaultChannelUniqueName = channelHash
        channelManager.populateChannels {
            this@ChatActivity.channelManager
                .getChannel(
                    this@ChatActivity.channelHash,
                    statusListener
                )
        }
    }

    private val statusListener = object : StatusListener() {
        override fun onSuccess() {
            channelManager.selectedChannel?.let { it1 -> loadData(it1) }
        }

        override fun onError(errorInfo: ErrorInfo?) {
            longToast(errorInfo?.message)
            binding?.layoutShimmer?.shimmerViewContainer?.gone()
        }
    }

    private fun loadData(selectedChannel: Channel) {
        currentChannel = selectedChannel
        messageRecycleAdapter.userPicture = if(intent.getIntExtra(Constants.BundleData.BUSINESS_ID,-1) != -1) selectedChannel.attributes.jsonObject?.optString(
            Constants.Twilio.TWILIO_PICTURE2,
            ""
        ) ?: "" else selectedChannel.attributes.jsonObject?.optString(
            Constants.Twilio.TWILIO_PICTURE,
            ""
        ) ?: ""
        viewModel.toolbarTitle.postValue(if(intent.getIntExtra(Constants.BundleData.BUSINESS_ID,-1) != -1) selectedChannel.attributes.jsonObject?.optString(
            Constants.Twilio.TWILIO_USERNAME2,
            ""
        ) ?: "" else selectedChannel.attributes.jsonObject?.optString(
            Constants.Twilio.TWILIO_USERNAME,
            ""
        ) ?: "")
        messagesObject = currentChannel?.messages
        currentChannel?.addListener(this)
        binding?.layoutShimmer?.shimmerViewContainer?.gone()
        viewModel.loadMessages(messagesObject)
    }

    private fun clearData() {
        binding?.layoutShimmer?.shimmerViewContainer?.visible()
        messageRecycleAdapter.clear()
        isFinished = false
        viewModel.last_loaded_message_index = 0
    }

    private fun messagesResultObserver() {
        viewModel.messagesResult.observe(this, Observer {
            when (it) {
                is Result.Loading -> {
                    loading.postValue(true)
                }
                is Result.Success -> {
                    it.data?.let { it1 ->
                        messageRecycleAdapter.submitItemsToTop(it1)
                        binding?.recyclerView?.scrollToPosition(it1.size - 1)
                    }
                    loading.postValue(false)
                    showHideNoData()
                }
                is Result.Error -> {
                    loading.postValue(false)
                    handleError(it.throwable)
                    showHideNoData()
                }
                is Result.CustomError -> {
                    loading.postValue(false)
                    showHideNoData()
                    if (it.errorCode == 0)
                        isFinished = true
                    else
                        HandleRequestFailedUtil.showDialogMessage(
                            it.message,
                            this
                        )
                }
            }
        })
    }

    private fun loadingObserver() {
        loading.observe(this, Observer {
            when (it) {
                true -> {
                    layout_loadmore?.visible()
                }
                false -> {
                    layout_loadmore?.gone()
                }
            }
        })
    }

    private fun showHideNoData() {
        if (messageRecycleAdapter.itemCount == 0) {
            binding?.layoutNoChat?.linearNoChat?.visible()
        } else {
            binding?.layoutNoChat?.linearNoChat?.gone()
        }
    }

    private fun setUpMessageRecyclerView() {
        messageRecycleAdapter =
            MessageRecyclerAdapter(
                this,
                if(intent.getIntExtra(Constants.BundleData.BUSINESS_ID,-1) == -1) viewModel.getMyId() else intent.getIntExtra(Constants.BundleData.BUSINESS_ID,-1).toString(),
                intent.getStringExtra(Constants.Twilio.USER_PICTURE) ?: "",
                object : Paginate.Callbacks {
                    override fun onLoadMore() {
                        if (loading.value == false && !isFinished && messageRecycleAdapter.itemCount > 0) {
                            viewModel.loadMessages(messagesObject, true)
                        }
                    }

                    override fun isLoading(): Boolean {
                        return loading.value ?: false
                    }

                    override fun hasLoadedAllItems(): Boolean {
                        return isFinished
                    }
                })
        binding?.recyclerView?.adapter = messageRecycleAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        binding?.recyclerView?.addItemDecoration(
            VerticalSpaceDecoration(
                0, resources.getDimension(R.dimen._10sdp).toInt()
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Message
        view?.let {
            refreshDataOnResume = false
            ViewImageActivity.start(this, item.media, it as ImageView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            ImagePickerUtil.TAKE_USER_IMAGE_REQUEST_CODE -> {
                data?.data?.let { sendImage(it) }
            }

        }
    }

    private fun sendImage(image: Uri) {

        messagesObject!!.sendMessage(
            Message.options()
                .withMedia(FileInputStream(image.path), "image/*")
                .withMediaFileName("image.jpg")
                .withMediaProgressListener(object : ProgressListener() {
                    override fun onStarted() {
                        binding?.progressBarr?.progressBar?.visible()
                    }

                    override fun onProgress(bytes: Long) {
                    }

                    override fun onCompleted(mediaSid: String?) {
                        binding?.progressBarr?.progressBar?.gone()
                    }
                }),
            object : CallbackListener<Message>() {
                override fun onSuccess(msg: Message) {
                    binding?.progressBarr?.progressBar?.gone()
                }

                override fun onError(error: ErrorInfo) {
                    longToast(error.message)
                    binding?.progressBarr?.progressBar?.gone()
                }
            })
    }

    override fun onChannelDeleted(p0: Channel?) {

    }

    override fun onInvitedToChannelNotification(p0: String?) {

    }

    override fun onClientSynchronization(p0: ChatClient.SynchronizationStatus?) {

    }

    override fun onNotificationSubscribed() {

    }

    override fun onUserSubscribed(p0: User?) {

    }

    override fun onChannelUpdated(p0: Channel?, p1: Channel.UpdateReason?) {

    }

    override fun onRemovedFromChannelNotification(p0: String?) {

    }

    override fun onNotificationFailed(p0: ErrorInfo?) {

    }

    override fun onTokenExpired() {

    }

    override fun onChannelJoined(p0: Channel?) {

    }

    override fun onChannelAdded(p0: Channel?) {

    }

    override fun onChannelSynchronizationChange(p0: Channel?) {

    }

    override fun onUserUnsubscribed(p0: User?) {

    }

    override fun onAddedToChannelNotification(p0: String?) {

    }

    override fun onChannelInvited(p0: Channel?) {

    }

    override fun onNewMessageNotification(p0: String?, p1: String?, p2: Long) {

    }

    override fun onConnectionStateChange(p0: ChatClient.ConnectionState?) {

    }

    override fun onError(p0: ErrorInfo?) {

    }

    override fun onUserUpdated(p0: User?, p1: User.UpdateReason?) {

    }

    override fun onTokenAboutToExpire() {

    }

    override fun onMemberDeleted(p0: Member?) {

    }

    override fun onTypingEnded(p0: Channel?, p1: Member?) {

    }

    override fun onMessageAdded(p0: Message?) {
        p0?.let {
            messageRecycleAdapter.submitItem(it)
            binding?.recyclerView?.smoothScrollToPosition(messageRecycleAdapter.itemCount - 1)
            if (messageRecycleAdapter.items.size == 1)
                showHideNoData()
        }
    }

    override fun onMessageDeleted(p0: Message?) {

    }

    override fun onMemberAdded(p0: Member?) {

    }

    override fun onTypingStarted(p0: Channel?, p1: Member?) {

    }

    override fun onSynchronizationChanged(p0: Channel?) {

    }

    override fun onMessageUpdated(
        p0: Message?,
        p1: Message.UpdateReason?
    ) {

    }

    override fun onMemberUpdated(p0: Member?, p1: Member.UpdateReason?) {

    }


    companion object {

        fun start(
            context: Context?,
            channelId: String,
            userPicture: String? = "",
            name:String? = "",
            businessId: Int? = null
        ) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(Constants.Twilio.CHANNEL_ID, channelId)
            intent.putExtra(Constants.Twilio.USER_PICTURE, userPicture)
            intent.putExtra(Constants.Twilio.USER_NAME, name)
            intent.putExtra(Constants.BundleData.BUSINESS_ID, businessId)
            context?.startActivity(intent)
        }
    }
}
