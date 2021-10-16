package com.technzone.minibursa.ui.subscription.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.enums.BusinessTypeEnums
import com.technzone.minibursa.data.models.subscrption.Subscription
import com.technzone.minibursa.databinding.FragmentBusinessSubscriptionBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.business.businessmain.fragments.listing.dialogs.SelectBusinessTypeDialog
import com.technzone.minibursa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.minibursa.ui.subscription.adapter.BusinessSubscriptionRecyclerAdapter
import com.technzone.minibursa.ui.subscription.viewmodel.SubscriptionViewModel
import com.technzone.minibursa.utils.extensions.showErrorAlert
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_ios.*
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
            subscriptionRecyclerAdapter.getSelectedItem().let {
                if (it == null) {
                    requireActivity().showErrorAlert(
                        getString(R.string.app_name),
                        getString(R.string.please_select_subscription)
                    )
                } else {
                    showSelectTypeDialog()
                }
            }
        }
    }

    private fun showSelectTypeDialog() {
        SelectBusinessTypeDialog(requireActivity(), object : SelectBusinessTypeDialog.CallBack {
            override fun callBack(businessTypeEnums: BusinessTypeEnums) {
                if(requireActivity().intent.getBooleanExtra(Constants.BundleData.CLEAR_TASK,false)){
                    requireActivity().finish()
                }
                CreateBusinessActivity.start(
                    context = requireActivity(),
                    businessType = businessTypeEnums.value,
                    hasBusiness = requireActivity().intent.getBooleanExtra(
                        Constants.BundleData.HAS_BUSINESS,
                        false
                    ),
                    companyDraft = false
                )
            }
        }).show()
    }

    private fun setUpSelectedCountriesAdapter() {
        subscriptionRecyclerAdapter = BusinessSubscriptionRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = subscriptionRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)

    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
//        item as GeneralLookup
    }

}