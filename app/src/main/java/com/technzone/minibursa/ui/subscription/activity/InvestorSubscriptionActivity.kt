package com.technzone.minibursa.ui.subscription.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.models.subscrption.Subscription
import com.technzone.minibursa.databinding.FragmentInvestorSubscriptionBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.subscription.adapter.InvestorSubscriptionRecyclerAdapter
import com.technzone.minibursa.ui.subscription.viewmodel.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class InvestorSubscriptionActivity : BaseBindingActivity<FragmentInvestorSubscriptionBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var subscriptionRecyclerAdapter: InvestorSubscriptionRecyclerAdapter
    private val viewModel: SubscriptionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.fragment_investor_subscription,
            hasToolbar = true,
            toolbarView = toolbar,
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
        subscriptionRecyclerAdapter = InvestorSubscriptionRecyclerAdapter(this)
        binding?.recyclerView?.adapter = subscriptionRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        subscriptionRecyclerAdapter.submitItems(
            arrayListOf(
                Subscription(),
                Subscription(),
                Subscription()
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
//        item as GeneralLookup
    }

    companion object {
        fun start(
            context: Context?,
            businessId: Int
        ) {
            val intent = Intent(context, InvestorSubscriptionActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS_ID, businessId)
            }
            context?.startActivity(intent)
        }
    }

}