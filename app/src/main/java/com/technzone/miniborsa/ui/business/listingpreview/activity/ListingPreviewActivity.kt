package com.technzone.miniborsa.ui.business.listingpreview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.Constants.MIN_PERCENTAGE_TO_SEND
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.business.ListingItem
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.databinding.ActivityListingPreviewBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.miniborsa.ui.business.listingpreview.adapters.ListingItemAdapter
import com.technzone.miniborsa.ui.business.listingpreview.dialog.SubmittedDialogFragment
import com.technzone.miniborsa.ui.business.listingpreview.viewmodel.ListingPreviewViewModel
import com.technzone.miniborsa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.miniborsa.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class ListingPreviewActivity : BaseBindingActivity<ActivityListingPreviewBinding>() {


    private val viewModel: ListingPreviewViewModel by viewModels()

    private lateinit var listingItemAdapter: ListingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleReceivedData()
        setContentView(
            R.layout.activity_listing_preview,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.list_business
        )
        setUpBinding()
        setUpListeners()
        setUpRvExtraInfo()
    }

    private fun handleReceivedData() {
        viewModel.hasBusiness = intent.getBooleanExtra(
            Constants.BundleData.HAS_BUSINESS, false
        )
        viewModel.companyDraft = intent.getBooleanExtra(
            Constants.BundleData.COMPANY_DRAFT, false
        )
        viewModel.businessDraft = intent.getBooleanExtra(
            Constants.BundleData.BUSINESS_DRAFT, false
        )
        intent.getSerializableExtra(Constants.BundleData.OWNER_BUSINESS)?.let { item ->
            item as OwnerBusiness
            item.icon?.let {
                if (item.images == null)
                    item.images = mutableListOf()
                item.images?.add(0, Media(name = it, id = -1))
            }
            viewModel.business = item
            viewModel.percentage.postValue(item.calculatePercentage())
        }
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
        binding?.layoutListingPending?.item = viewModel.business
    }

    private fun setUpListeners() {
        binding?.btnSubmit?.setOnClickListener {
            if (viewModel.percentage.value ?: 0 > MIN_PERCENTAGE_TO_SEND)
                viewModel.business?.id?.let { it1 ->
                    viewModel.sendRequestBusiness(it1).observe(this, sendRequestResultObserver())
                }
            else {
                InvestorMainActivity.start(this)
            }
        }
        binding?.layoutListingPending?.tvStatus?.setOnClickListener {
            if (viewModel.hasBusiness)
                viewModel.business?.id?.let { it1 ->
                    viewModel.deleteBusinessRequest(it1)
                        .observe(this, deleteRequestResultObserver())
                }
            else
                viewModel.deleteCompanyRequest().observe(this, deleteRequestResultObserver())
        }
    }

    private fun showSubmittedDialog() {
        val dialog = SubmittedDialogFragment(this)
        dialog.setOnDismissListener {
            if (viewModel.isHasBusiness())
                BusinessMainActivity.start(this)
            else{
                InvestorMainActivity.start(this)
            }
        }
        dialog.show()
    }


    private fun sendRequestResultObserver(
    ): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    showSubmittedDialog()
                }
            })
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
                    InvestorMainActivity.start(this@ListingPreviewActivity)
                }
            })
    }

    private fun setUpRvExtraInfo() {
        listingItemAdapter = ListingItemAdapter(this)
        binding?.rvListingItems?.adapter = listingItemAdapter
        listingItemAdapter.submitItems(
            arrayListOf(
                ListingItem(
                    title = "Title",
                    description = "Lorem ipsum dolor sit amet.",
                    percent = viewModel.business?.calculateFirstStepPercentage() ?: 0
                ),
                ListingItem(
                    title = "Location Info",
                    description = "Lorem ipsum dolor sit amet.",
                    percent = viewModel.business?.calculateSecondStepPercentage() ?: 0
                ),
                ListingItem(
                    title = "Media",
                    description = "Lorem ipsum dolor sit amet.",
                    percent = viewModel.business?.calculateThirdPercentage() ?: 0
                ),
                ListingItem(
                    title = "More Details",
                    description = "Lorem ipsum dolor sit amet.",
                    percent = viewModel.business?.calculateFourthPercentage() ?: 0
                )
            )
        )
    }

    companion object {
        fun start(
            context: Context?,
            business: OwnerBusiness? = null,
            hasBusiness: Boolean = true,
            companyDraft: Boolean = false,
            businessDraft: Boolean = false
        ) {
            val intent = Intent(context, ListingPreviewActivity::class.java).apply {
                putExtra(Constants.BundleData.OWNER_BUSINESS, business)
                putExtra(Constants.BundleData.HAS_BUSINESS, hasBusiness)
                putExtra(Constants.BundleData.COMPANY_DRAFT, companyDraft)
                putExtra(Constants.BundleData.BUSINESS_DRAFT, businessDraft)
            }
            context?.startActivity(intent)
        }
    }
}