package com.technzone.miniborsa.ui.subscription.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.subscrption.Subscription
import com.technzone.miniborsa.databinding.FragmentBusinessSubscriptionBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.subscription.adapter.BusinessSubscriptionRecyclerAdapter
import com.technzone.miniborsa.ui.subscription.viewmodel.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class BusinessSubscriptionFragment : BaseBindingFragment<FragmentBusinessSubscriptionBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: SubscriptionViewModel by activityViewModels()

    lateinit var subscriptionRecyclerAdapter: BusinessSubscriptionRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_business_subscription

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
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
        subscriptionRecyclerAdapter = BusinessSubscriptionRecyclerAdapter(requireContext())
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

}