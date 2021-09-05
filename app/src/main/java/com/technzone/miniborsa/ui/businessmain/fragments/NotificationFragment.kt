package com.technzone.miniborsa.ui.businessmain.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.FragmentNotificationBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.invistormain.viewmodels.InvestorMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseBindingFragment<FragmentNotificationBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }


    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}