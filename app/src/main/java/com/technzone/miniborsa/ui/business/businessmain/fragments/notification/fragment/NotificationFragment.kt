package com.technzone.miniborsa.ui.business.businessmain.fragments.notification.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.notification.Notification
import com.technzone.miniborsa.databinding.FragmentNotificationBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.businessmain.fragments.notification.adapters.NotificationsAdapter
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseBindingFragment<FragmentNotificationBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by activityViewModels()
    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpNotification()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

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

}