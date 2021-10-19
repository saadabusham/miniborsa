package com.technzone.minibursa.ui.core.messages

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.technzone.minibursa.R
import com.technzone.minibursa.common.MyApplication
import com.technzone.minibursa.common.interfaces.LoginCallBack
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.api.response.Result
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.Constants.Twilio.TWILIO_PICTURE
import com.technzone.minibursa.data.common.Constants.Twilio.TWILIO_PICTURE2
import com.technzone.minibursa.data.common.Constants.Twilio.TWILIO_USERNAME
import com.technzone.minibursa.data.common.Constants.Twilio.TWILIO_USERNAME2
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.databinding.FragmentMessagesBinding
import com.technzone.minibursa.ui.auth.AuthActivity
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.minibursa.ui.core.chat.ChatActivity
import com.technzone.minibursa.ui.core.messages.adapters.ChannelsRecyclerAdapter
import com.technzone.minibursa.ui.core.messages.viewmodels.MessagesViewModel
import com.technzone.minibursa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.minibursa.ui.investor.invistorroles.activity.InvestorRolesActivity
import com.technzone.minibursa.utils.extensions.*
import com.technzone.minibursa.utils.recycleviewutils.DividerItemDecorator
import com.technzone.minibursa.utils.twilio.chat.channels.ChannelManager
import com.technzone.minibursa.utils.twilio.chat.client.ChatClientManager
import com.technzone.minibursa.utils.twilio.chat.listeners.TaskCompletionListener
import com.twilio.chat.Channel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : BaseBindingFragment<FragmentMessagesBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener, LoginCallBack {

    private val viewModel: MessagesViewModel by activityViewModels()

    private var chatClientManager: ChatClientManager? = null
    private lateinit var channelsRecyclerAdapter: ChannelsRecyclerAdapter
    private lateinit var channelManager: ChannelManager
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val channelsMutableLiveData: MutableLiveData<Result<MutableList<Channel>>> =
        MutableLiveData()
    private var business: OwnerBusiness? = null

    override fun getLayoutId(): Int = R.layout.fragment_messages

    override fun onResume() {
        super.onResume()
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        handleNavigationView(false)
    }

    override fun onViewVisible() {
        super.onViewVisible()
        arguments?.getSerializable(Constants.BundleData.BUSINESS)?.let {
            it as OwnerBusiness?
            business = it
            binding?.imgBack?.visible()
            binding?.tvTitle?.text = it.title
        }
        handleNavigationView(true)
        setUpBinding()
        setUpListeners()
        observeLoading()
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
        binding?.imgBack?.setOnClickListener {
            navigationController.navigateUp()
        }
    }

    private fun handleNavigationView(hide: Boolean) {
        arguments?.getBoolean(Constants.BundleData.SHOW_BACK)?.let {
            if (it) {
                when (val activity = requireActivity()) {
                    is BusinessMainActivity -> {
                        if (hide) activity.hideBottomSheet() else activity.showBottomSheet()
                    }
                    is InvestorMainActivity -> {
                        if (hide) activity.hideBottomSheet() else activity.showBottomSheet()
                    }
                }
            }
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
        viewModel.getAccessToken(businessId = business?.id)
            .observe(this, accessTokenResultObserver())
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

                override fun onLoading() {
                    super.onLoading()
                    loading.postValue(true)
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    loading.postValue(false)
                    hideShowNoData()
                }
            }, withProgress = false
        )
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
//        channelsResultObserver()
//        channelManager.getAllChannel(channelsMutableLiveData)

        channelManager.getAllChannels().observe(this@MessagesFragment, channelResultObserver())

    }

    private fun channelResultObserver(): CustomObserverResponse<MutableList<Channel>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<MutableList<Channel>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: MutableList<Channel>?
                ) {
                    if (data.isNullOrEmpty()) {
                        loading.postValue(false)
                        hideShowNoData()
                    } else {
                        setData(data)
                        loading.postValue(false)
                    }
                }

                override fun onLoading() {
                    super.onLoading()
                    loading.postValue(true)
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    loading.postValue(false)
                    hideShowNoData()
                }
            }, withProgress = false, showError = false
        )
    }

    private fun channelsResultObserver() {
        channelsMutableLiveData.observe(this, {
            when (it) {
                is Result.Loading -> {
                    loading.postValue(true)
                }
                is Result.Success -> {
                    if (it.data.isNullOrEmpty()) {
                        loading.postValue(false)
                        hideShowNoData()
                    } else {
                        setData(it.data)
                    }
                }
                is Result.Error -> {
                    loading.postValue(false)
                    hideShowNoData()
                }
                is Result.CustomError -> {
                    loading.postValue(false)
                    hideShowNoData()
                }
            }
        })
    }

    private fun setData(data: MutableList<Channel>?) {
        data?.toMutableList()?.let { channelsRecyclerAdapter.submitItems(it) }
    }

    private fun hideShowNoData() {
        if (channelsRecyclerAdapter.itemCount == 0) {
            binding?.layoutNoData?.root?.visible()
        } else {
            binding?.layoutNoData?.root?.gone()
        }
    }

    private fun observeLoading() {
        loading.observe(this, {
            if (it) {
                (requireActivity() as BaseBindingActivity<*>).binding?.root?.disableViews()
                binding?.recyclerView?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
                (requireActivity() as BaseBindingActivity<*>).binding?.root?.enableViews()
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.recyclerView?.visible()
            }
        })
    }

    private fun setUpRecyclerView() {
        channelsRecyclerAdapter = ChannelsRecyclerAdapter(requireContext(), business != null)
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
                context = requireContext(),
                channelId = item.sid,
                userPicture = if (business != null) item.attributes.jsonObject?.optString(
                    TWILIO_PICTURE2,
                    ""
                ) ?: "" else item.attributes.jsonObject?.optString(
                    TWILIO_PICTURE,
                    ""
                ) ?: "",
                businessId = if (business != null) business?.id else null,
                name = if (business != null) item.attributes.jsonObject?.optString(
                    TWILIO_USERNAME2,
                    ""
                ) ?: "" else item.attributes.jsonObject?.optString(
                    TWILIO_USERNAME,
                    ""
                ) ?: "",
            )
    }

}