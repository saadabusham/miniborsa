package com.technzone.minibursa.ui.business.businesschannels.fragments

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.databinding.FragmentBusinessChannelsBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.business.businesschannels.adapters.BusinessChannelsAdapter
import com.technzone.minibursa.ui.business.businessmain.viewmodels.BusinessMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessChannelsFragment : BaseBindingFragment<FragmentBusinessChannelsBinding>() {

    private val viewModel: BusinessMainViewModel by activityViewModels()
    private lateinit var listingAdapter: BusinessChannelsAdapter

    override fun getLayoutId(): Int = R.layout.fragment_business_channels

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpListing()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun loadData() {
        getListing()
    }

    private fun getListing() {
        viewModel.getListing().observe(this, listingResultObserver())
    }

    private fun setUpListing() {
        listingAdapter = BusinessChannelsAdapter(requireContext())
        binding?.recyclerView?.adapter = listingAdapter
        binding?.recyclerView?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                item as OwnerBusiness
                navigationController.navigate(
                    R.id.action_nav_business_channels_to_messagesFragment, bundleOf(
                        Pair(Constants.BundleData.BUSINESS, item),
                        Pair(Constants.BundleData.SHOW_BACK, true)
                    )
                )
            }
        })
    }

    private fun listingResultObserver(): CustomObserverResponse<ListWrapper<OwnerBusiness>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<OwnerBusiness>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<OwnerBusiness>?
                ) {
                    data?.data?.let {
                        listingAdapter.submitNewItems(it)
                    }
                    handleListingViews()
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    handleListingViews()
                }
            })
    }

    private fun handleListingViews() {
        if (listingAdapter.itemCount > 0) {

        } else {

        }
    }

}