package com.technzone.miniborsa.ui.core.notification.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
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

    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun onViewVisible() {
        super.onViewVisible()
        handleNavigationView(true)
        setUpBinding()
        setUpListeners()
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
        binding?.layoutToolbarWithAction?.imgAction?.visible()
        binding?.layoutToolbarWithAction?.imgAction?.setOnClickListener {

        }
        binding?.layoutToolbarWithAction?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setUpNotification() {
        notificationsAdapter = NotificationsAdapter(requireContext())
        binding?.rvNotifications?.adapter = notificationsAdapter
        binding?.rvNotifications?.setOnItemClickListener(this)
        notificationsAdapter.submitItems(
            arrayListOf(
                Notification(),
                Notification(),
                Notification(),
                Notification(),
                Notification(),
                Notification(),
                Notification()
            )
        )
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