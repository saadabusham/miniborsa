package com.technzone.minibursa.ui.subscription.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.databinding.FragmentInvestorSubscriptionBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.subscription.adapter.InvestorSubscriptionRecyclerAdapter
import com.technzone.minibursa.ui.subscription.viewmodel.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorSubscriptionFragment : BaseBindingFragment<FragmentInvestorSubscriptionBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: SubscriptionViewModel by activityViewModels()

    lateinit var subscriptionRecyclerAdapter: InvestorSubscriptionRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_investor_subscription

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
            hasToolbar = true,
            toolbarView =  binding?.layoutToolbar?.toolbar,
            tvToolbarTitleView = binding?.layoutToolbar?.tvToolbarTitle,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.subscription
        )
        setUpBinding()
        setUpListeners()
        setUpSelectedCountriesAdapter()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnContinue?.setOnClickListener {

        }
    }

    private fun setUpSelectedCountriesAdapter() {
        subscriptionRecyclerAdapter = InvestorSubscriptionRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = subscriptionRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
//        item as GeneralLookup
    }

}