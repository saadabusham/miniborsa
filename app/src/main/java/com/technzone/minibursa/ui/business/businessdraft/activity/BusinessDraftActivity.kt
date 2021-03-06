package com.technzone.minibursa.ui.business.businessdraft.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.BusinessStatusEnums
import com.technzone.minibursa.data.enums.BusinessTypeEnums
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.databinding.ActivityBusinessDraftBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.business.businessdraft.viewmodels.BusinessDraftViewModel
import com.technzone.minibursa.ui.business.businessmain.fragments.listing.adapters.ListingReviewAdapter
import com.technzone.minibursa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.minibursa.ui.business.listingpreview.activity.ListingPreviewActivity
import com.technzone.minibursa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.minibursa.utils.extensions.gone
import com.technzone.minibursa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class BusinessDraftActivity : BaseBindingActivity<ActivityBusinessDraftBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: BusinessDraftViewModel by viewModels()
    private lateinit var listingReviewAdapter: ListingReviewAdapter
    override fun onResume() {
        super.onResume()
        viewModel.getPendingListing().observe(this, pendingResultObserver())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_business_draft,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.my_listing
        )
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
                BusinessTypeEnums.BUSINESS_FOR_SALE.value,
                hasBusiness = false
            )
        }
        binding?.layoutAddBusiness?.btnBusinessForShare?.setOnClickListener {
            CreateBusinessActivity.start(
                this,
                BusinessTypeEnums.BUSINESS_FOR_SHARE.value,
                hasBusiness = false
            )
        }
        binding?.layoutAddBusiness?.btnBusinessFranchise?.setOnClickListener {
            CreateBusinessActivity.start(
                this,
                BusinessTypeEnums.BUSINESS_FRANCHISE.value,
                hasBusiness = false
            )
        }
    }

    private fun setUpListingPending() {
        listingReviewAdapter = ListingReviewAdapter(this, true)
        binding?.layoutListing?.rvPending?.adapter = listingReviewAdapter
        binding?.layoutListing?.rvPending?.setOnItemClickListener(this)
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
                        listingReviewAdapter.clear()
                        listingReviewAdapter.submitItem(it)
                        binding?.layoutListing?.constraintRoot?.visible()
                        binding?.layoutListing?.tvPending?.visible()
                        binding?.layoutListing?.rvPending?.visible()
                    } ?: also {
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
        if (view?.id == R.id.tvStatus) {
            viewModel.deleteCompanyRequest().observe(this, deleteRequestResultObserver())
        } else {
            if (item.status == BusinessStatusEnums.DRAFT.value)
                CreateBusinessActivity.start(
                    this,
                    businessType = -1,
                    business = item,
                    hasBusiness = false,
                    companyDraft = true
                )
            else {
                ListingPreviewActivity.start(
                    this,
                    business = item,
                    hasBusiness = false,
                    companyDraft = true
                )
            }
        }
    }

    private fun deleteRequestResultObserver(
    ): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    InvestorMainActivity.start(this@BusinessDraftActivity)
                }
            })
    }

    companion object {
        fun start(
            context: Activity?,
            forResult: Boolean = false
        ) {
            val intent = Intent(context, BusinessDraftActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            if (forResult) {
                context?.startActivityForResult(intent, 0)
            } else {
                context?.startActivity(intent)
            }
        }
    }

}