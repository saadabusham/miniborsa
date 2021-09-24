package com.technzone.miniborsa.ui.core.messages

import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.MyApplication
import com.technzone.miniborsa.common.interfaces.LoginCallBack
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.api.response.Result
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.databinding.FragmentMessagesBinding
import com.technzone.miniborsa.ui.auth.AuthActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.core.messages.adapters.ChannelsRecyclerAdapter
import com.technzone.miniborsa.ui.core.messages.viewmodels.MessagesViewModel
import com.technzone.miniborsa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.miniborsa.ui.investor.invistorroles.activity.InvestorRolesActivity
import com.technzone.miniborsa.ui.twilio.chat.ChatActivity
import com.technzone.miniborsa.ui.userrole.activity.UserRolesActivity
import com.technzone.miniborsa.utils.HandleRequestFailedUtil
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.showErrorAlert
import com.technzone.miniborsa.utils.extensions.visible
import com.technzone.miniborsa.utils.recycleviewutils.DividerItemDecorator
import com.technzone.miniborsa.utils.twilio.chat.channels.ChannelManager
import com.technzone.miniborsa.utils.twilio.chat.client.ChatClientManager
import com.technzone.miniborsa.utils.twilio.chat.listeners.TaskCompletionListener
import com.twilio.chat.Channel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : BaseBindingFragment<FragmentMessagesBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener, LoginCallBack {

    private val viewModel: MessagesViewModel by activityViewModels()

    private var chatClientManager: ChatClientManager? = null
    private lateinit var channelsRecyclerAdapter: ChannelsRecyclerAdapter
    private lateinit var channelManager: ChannelManager
    private val channelsMutableLiveData: MutableLiveData<Result<MutableList<Channel>>> =
        MutableLiveData()

    override fun getLayoutId(): Int = R.layout.fragment_messages

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun init() {
        if (viewModel.isUserLoggedIn()) {
            checkCanViewMessages()
        } else {
            if (requireActivity() is InvestorMainActivity) (requireActivity() as InvestorMainActivity).loginCallBack =
                this
            hideShowLoginLayouts(false)
        }
    }

    private fun checkCanViewMessages() {
        if (viewModel.canViewMessages()) {
            hideShowLoginLayouts(true)
            hideShowNotAuthorizedLayouts(true)
            setUpRecyclerView()
            getChannels()
        } else {
            hideShowNotAuthorizedLayouts(false)
        }
    }

    private fun setUpListeners() {
        binding?.layoutNotLoggedIn?.btnlogin?.setOnClickListener {
            AuthActivity.startForResult(requireActivity(), true)
        }
        binding?.layoutNotAuthorized?.btnBecomeInvestor?.setOnClickListener {
            InvestorRolesActivity.start(requireContext())
        }
    }

    private fun hideShowLoginLayouts(show: Boolean) {
        if (!show) {
            binding?.layoutNotLoggedIn?.linearNotLoggedIn?.visible()
        } else {
            binding?.layoutNotLoggedIn?.linearNotLoggedIn?.gone()
        }
    }

    private fun hideShowNotAuthorizedLayouts(show: Boolean) {
        if (!show) {
            binding?.layoutNotAuthorized?.linearRoot?.visible()
        } else {
            binding?.layoutNotAuthorized?.linearRoot?.gone()
        }
    }

    private fun getChannels() {
        chatClientManager = MyApplication.instance.chatClientManager
        viewModel.getAccessToken().observe(this, accessTokenResultObserver())
    }

    private fun accessTokenResultObserver(): CustomObserverResponse<String> {
        return CustomObserverResponse(
            requireActivity(),
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
        if (chatClientManager!!.getChatClient() == null) {
            initializeClient()
        } else {
            handleChannel()
        }
    }

    private fun initializeClient() {
        chatClientManager?.buildClient(
            viewModel.token,
            object : TaskCompletionListener<Void?, String?> {
                override fun onSuccess(aVoid: Void?) {
                    println("===>client created")
                    handleChannel()
                }

                override fun onError(errorMessage: String?) {
                    binding?.layoutShimmer?.shimmerViewContainer?.gone()
                    requireActivity().showErrorAlert(
                        resources.getString(R.string.app_name),
                        "Failed tot create client"
                    )
                }
            })
    }

    fun handleChannel() {
        channelManager = ChannelManager.instance
        channelsResultObserver()
        channelManager.getAllChannel(channelsMutableLiveData)
    }

    private fun channelsResultObserver() {
        channelsMutableLiveData.observe(this, {
            when (it) {
                is Result.Loading -> {
                    binding?.layoutShimmer?.shimmerViewContainer?.visible()
                }
                is Result.Success -> {
                    object : CountDownTimer(1000, 1000) {
                        override fun onFinish() {
                            val sortedArray = it.data?.sortedByDescending {
                                it.lastMessageDate
                            }
                            sortedArray?.let { it1 -> channelsRecyclerAdapter.submitItems(it1) }
                            binding?.layoutShimmer?.shimmerViewContainer?.gone()
                        }

                        override fun onTick(millisUntilFinished: Long) {
                        }

                    }.start()
                }
                is Result.Error -> {
                    binding?.layoutShimmer?.shimmerViewContainer?.gone()
                }
                is Result.CustomError -> {
                    binding?.layoutShimmer?.shimmerViewContainer?.gone()
//                    HandleRequestFailedUtil.showDialogMessage(
//                        it.message,
//                        requireActivity()
//                    )
                }
            }
        })
    }

    private fun setUpRecyclerView() {
        channelsRecyclerAdapter = ChannelsRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = channelsRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        binding?.recyclerView?.addItemDecoration(
            DividerItemDecorator(resources.getDrawable(R.drawable.divider), 0, 0)
        )
    }

    override fun loggedInSuccess() {
        init()
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Channel)
            ChatActivity.start(
                requireContext(),
                item.sid,
                item.attributes.jsonObject?.optString("userId", "") ?: "",
                item.attributes.jsonObject?.optString("fullName", "") ?: "",
                item.attributes.jsonObject?.optString("picture", "") ?: ""
            )
    }

}