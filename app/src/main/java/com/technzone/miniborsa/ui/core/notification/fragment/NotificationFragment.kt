package com.technzone.miniborsa.ui.core.notification.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.paginate.Paginate
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.notification.Notification
import com.technzone.miniborsa.databinding.FragmentNotificationBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.miniborsa.ui.core.notification.adapters.NotificationsAdapter
import com.technzone.miniborsa.ui.core.notification.viewmodels.NotificationsViewModel
import com.technzone.miniborsa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseBindingFragment<FragmentNotificationBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: NotificationsViewModel by activityViewModels()
    private lateinit var notificationsAdapter: NotificationsAdapter

    private var page: Int = 1
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun onViewVisible() {
        super.onViewVisible()
        viewModel.clearNewNotifications()
        handleNavigationView(true)
        setUpBinding()
        setUpListeners()
        observeLoading()
        setUpNotification()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun handleNavigationView(hide: Boolean) {
        arguments?.getBoolean(Constants.BundleData.SHOW_BACK)?.let {
            if (it) {
                binding?.layoutToolbarWithAction?.imgBack?.visible()
                when (val activity = requireActivity()) {
                    is BusinessMainActivity -> {
                        if (hide) activity.hideBottomSheet() else activity.showBottomSheet()
                    }
                    is InvestorMainActivity -> {
                        if (hide) activity.hideBottomSheet() else activity.showBottomSheet()
                    }
                    else -> {
                    }
                }
            } else {
                binding?.layoutToolbarWithAction?.imgBack?.gone()
            }
        }
    }

    private fun setUpListeners() {
        binding?.layoutToolbarWithAction?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setUpNotification() {
        notificationsAdapter = NotificationsAdapter(requireContext())
        binding?.rvNotifications?.adapter = notificationsAdapter
        binding?.rvNotifications?.setOnItemClickListener(this)
        Paginate.with(binding?.rvNotifications, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && notificationsAdapter.itemCount > 0 && !isFinished) {
                    page++
                    loadData()
                }
            }

            override fun isLoading(): Boolean {
                return loading.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isFinished
            }

        })
            .setLoadingTriggerThreshold(1)
            .addLoadingListItem(false)
            .build()
        loadData()
    }

    private fun loadData() {
        viewModel.getNotifications(pageNumber = page).observe(this, notificationsResultObserver())
    }

    private fun notificationsResultObserver(): CustomObserverResponse<ListWrapper<Notification>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<Notification>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<Notification>?
                ) {
                    isFinished =
                        data?.data?.size?.plus(notificationsAdapter.itemCount) ?: 0 >= data?.totalRows ?: 0
                    data?.data?.let {
                        if (page == 1) {
                            notificationsAdapter.submitItems(it)
                        } else {
                            notificationsAdapter.addItems(it)
                        }
                    }
                    loading.postValue(false)
                    hideShowNoData()

                    notificationsAdapter.submitItem(Notification())
                    notificationsAdapter.submitItem(Notification())
                    notificationsAdapter.submitItem(Notification())
                    notificationsAdapter.submitItem(Notification())
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    loading.postValue(false)
                    hideShowNoData()
                    notificationsAdapter.submitItem(Notification())
                    notificationsAdapter.submitItem(Notification())
                    notificationsAdapter.submitItem(Notification())
                    notificationsAdapter.submitItem(Notification())
                }

                override fun onLoading() {
                    super.onLoading()
                    loading.postValue(true)
                }
            }, withProgress = false
        )
    }

    private fun hideShowNoData() {
        if (notificationsAdapter.itemCount == 0) {
            binding?.layoutNoData?.root?.visible()
        } else {
            binding?.layoutNoData?.root?.gone()
        }
    }


    private fun observeLoading() {
        loading.observe(this, Observer {
            if (it) {
                binding?.rvNotifications?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.rvNotifications?.visible()
            }
        })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        handleNavigationView(false)
    }
}