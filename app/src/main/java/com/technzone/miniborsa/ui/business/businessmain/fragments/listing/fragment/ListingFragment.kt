package com.technzone.miniborsa.ui.business.businessmain.fragments.listing.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.BusinessStatusEnums
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.databinding.FragmentListingBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.businessmain.fragments.listing.adapters.ListingAdapter
import com.technzone.miniborsa.ui.business.businessmain.fragments.listing.adapters.ListingReviewAdapter
import com.technzone.miniborsa.ui.business.businessmain.fragments.listing.dialogs.SelectBusinessTypeDialog
import com.technzone.miniborsa.ui.business.businessmain.viewmodels.BusinessMainViewModel
import com.technzone.miniborsa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.miniborsa.ui.business.listingpreview.activity.ListingPreviewActivity
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingFragment : BaseBindingFragment<FragmentListingBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: BusinessMainViewModel by activityViewModels()
    private lateinit var listingReviewAdapter: ListingReviewAdapter
    private lateinit var listingAdapter: ListingAdapter

    override fun getLayoutId(): Int = R.layout.fragment_listing

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpListingPending()
        setUpListing()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.layoutAddBusiness?.btnBusinessForSale?.setOnClickListener {
            CreateBusinessActivity.start(
                requireContext(),
                BusinessTypeEnums.BUSINESS_FOR_SALE.value,
                hasBusiness = true
            )
        }
        binding?.layoutAddBusiness?.btnBusinessForShare?.setOnClickListener {
            CreateBusinessActivity.start(
                requireContext(),
                BusinessTypeEnums.BUSINESS_FOR_SHARE.value,
                hasBusiness = true
            )
        }
        binding?.layoutAddBusiness?.btnBusinessFranchise?.setOnClickListener {
            CreateBusinessActivity.start(
                requireContext(),
                BusinessTypeEnums.BUSINESS_FRANCHISE.value,
                hasBusiness = true
            )
        }
        binding?.imgAddBusiness?.setOnClickListener {
            showSelectTypeDialog()
        }
    }

    private fun showSelectTypeDialog() {
        SelectBusinessTypeDialog(requireActivity(), object : SelectBusinessTypeDialog.CallBack {
            override fun callBack(businessTypeEnums: BusinessTypeEnums) {
                CreateBusinessActivity.start(
                    context = requireContext(),
                    businessType = businessTypeEnums.value,
                    hasBusiness = true
                )
            }
        }).show()
    }

    private fun setUpListingPending() {
        listingReviewAdapter = ListingReviewAdapter(requireContext())
        binding?.layoutListing?.rvPending?.adapter = listingReviewAdapter
        binding?.layoutListing?.rvPending?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                item as OwnerBusiness
                if (view?.id == R.id.tvStatus) {
                    item.id?.let {
                        viewModel.deleteBusinessRequest(it)
                            .observe(requireActivity(), deleteRequestResultObserver())
                    }
                } else {
                    if (item.status == BusinessStatusEnums.DRAFT.value || item.status == BusinessStatusEnums.D.value)
                        CreateBusinessActivity.start(
                            requireContext(),
                            businessType = -1,
                            business = item,
                            hasBusiness = true,
                            businessDraft = true
                        )
                    else {
                        ListingPreviewActivity.start(
                            requireContext(),
                            business = item,
                            hasBusiness = true,
                            businessDraft = true
                        )
                    }
                }
            }
        })
    }

    private fun loadData() {
        getPendingList()
        getListing()
    }

    private fun getPendingList() {
        viewModel.getPendingListing().observe(this, pendingResultObserver())
    }

    private fun getListing() {
        viewModel.getListing().observe(this, listingResultObserver())
    }

    private fun deleteRequestResultObserver(
    ): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    getPendingList()
                }
            })
    }


    private fun pendingResultObserver(): CustomObserverResponse<ListWrapper<OwnerBusiness>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<OwnerBusiness>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<OwnerBusiness>?
                ) {
                    data?.data?.let {
                        listingReviewAdapter.submitNewItems(it)
                    }
                    handlePendingListingViews()
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    handlePendingListingViews()
                }
            })
    }

    private fun handlePendingListingViews() {
        if (listingReviewAdapter.itemCount > 0) {
            binding?.layoutListing?.constraintRoot?.visible()
            binding?.layoutListing?.tvPending?.visible()
            binding?.layoutListing?.rvPending?.visible()
            binding?.imgAddBusiness?.visible()
        } else {
            binding?.layoutListing?.tvPending?.gone()
            binding?.layoutListing?.rvPending?.gone()
        }
    }

    private fun setUpListing() {
        listingAdapter = ListingAdapter(requireContext())
        binding?.layoutListing?.rvListing?.adapter = listingAdapter
        binding?.layoutListing?.rvListing?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {

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
            binding?.layoutListing?.constraintRoot?.visible()
            binding?.layoutListing?.tvListing?.visible()
            binding?.layoutListing?.rvListing?.visible()
            binding?.imgAddBusiness?.visible()
        } else {
            binding?.layoutListing?.tvListing?.gone()
            binding?.layoutListing?.rvListing?.gone()
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}