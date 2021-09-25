package com.technzone.miniborsa.ui.business.businessdraft.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.databinding.ActivityBusinessDraftBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.business.businessdraft.viewmodels.BusinessDraftViewModel
import com.technzone.miniborsa.ui.business.businessmain.fragments.listing.adapters.ListingReviewAdapter
import com.technzone.miniborsa.ui.business.businessmain.fragments.listing.dialogs.SelectBusinessTypeDialog
import com.technzone.miniborsa.ui.business.businessmain.viewmodels.BusinessMainViewModel
import com.technzone.miniborsa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessDraftActivity : BaseBindingActivity<ActivityBusinessDraftBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: BusinessDraftViewModel by viewModels()
    private lateinit var listingReviewAdapter: ListingReviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_draft, hasToolbar = false)
        setUpBinding()
        setUpListeners()
        setUpListingPending()
    }


    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.layoutAddBusiness?.btnBusinessForSale?.setOnClickListener {
            CreateBusinessActivity.start(
                this,
                BusinessTypeEnums.BUSINESS_FOR_SALE.value
            )
        }
        binding?.layoutAddBusiness?.btnBusinessForShare?.setOnClickListener {
            CreateBusinessActivity.start(
                this,
                BusinessTypeEnums.BUSINESS_FOR_SHARE.value
            )
        }
        binding?.layoutAddBusiness?.btnBusinessFranchise?.setOnClickListener {
            CreateBusinessActivity.start(
                this,
                BusinessTypeEnums.BUSINESS_FRANCHISE.value
            )
        }
        binding?.imgAddBusiness?.setOnClickListener {
            SelectBusinessTypeDialog(this, object : SelectBusinessTypeDialog.CallBack {
                override fun callBack(businessTypeEnums: BusinessTypeEnums) {
                    CreateBusinessActivity.start(
                        this@BusinessDraftActivity,
                        businessTypeEnums.value
                    )
                }
            }).show()
        }
    }

    private fun setUpListingPending() {
        listingReviewAdapter = ListingReviewAdapter(this)
        binding?.layoutListing?.rvPending?.adapter = listingReviewAdapter
        binding?.layoutListing?.rvPending?.setOnItemClickListener(this)
        viewModel.getPendingListing().observe(this, pendingResultObserver())
    }

    private fun pendingResultObserver(): CustomObserverResponse<OwnerBusiness> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<OwnerBusiness> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: OwnerBusiness?
                ) {
                    data?.let {
                        listingReviewAdapter.submitItem(it)
                        binding?.layoutListing?.constraintRoot?.visible()
                        binding?.layoutListing?.tvPending?.visible()
                        binding?.layoutListing?.rvPending?.visible()
                        binding?.imgAddBusiness?.visible()
                    }?.also {
                        binding?.layoutListing?.tvPending?.gone()
                        binding?.layoutListing?.rvPending?.gone()
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    binding?.layoutListing?.tvPending?.gone()
                    binding?.layoutListing?.rvPending?.gone()
                }
            })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as OwnerBusiness
        if(view?.id == R.id.tvStatus){

        }
    }

    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, BusinessDraftActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

}