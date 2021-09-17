package com.technzone.miniborsa.ui.investor.businessdetails.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.databinding.ActivityBusinessDetailsBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.dataview.viewimage.ViewImageActivity
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessDocumentAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessExtraInfoAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessFieldAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessSliderAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.viewmodels.BusinessDetailsViewModel
import com.technzone.miniborsa.ui.subscription.activity.SubscriptionActivity
import com.technzone.miniborsa.utils.extensions.getSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_business_details_toolbar.*

@AndroidEntryPoint
class BusinessDetailsActivity : BaseBindingActivity<ActivityBusinessDetailsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: BusinessDetailsViewModel by viewModels()
    private lateinit var businessSliderAdapter: BusinessSliderAdapter
    private lateinit var businessFieldAdapter: BusinessFieldAdapter
    private lateinit var businessExtraInfoAdapter: BusinessExtraInfoAdapter
    private lateinit var businessDocumentAdapter: BusinessDocumentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_business_details,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = false
        )
        setUpBinding()
        setUpListeners()
        initData()
    }

    private fun initData() {
        intent.getIntExtra(Constants.BundleData.BUSINESS_ID, -1)?.let {
            if (it == -1) {
                viewModel.businessToView.value =
                    intent.getSerializableExtra(Constants.BundleData.BUSINESS) as Business
                handleData()
            } else
                viewModel.getBusiness(it).observe(this, businessDetailsResultObserver())
        }
    }

    private fun handleData() {
        setUpRvSlider()
        setUpRvFields()
        setUpRvExtraInfo()
        setUpRvDocuments()
    }

    private fun setUpListeners() {
        binding?.btnConnect?.setOnClickListener {
            SubscriptionActivity.start(this, false)
        }
        binding?.toolbar?.imgFavorite?.setOnClickListener {

        }
        binding?.toolbar?.imgShare?.setOnClickListener {

        }
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun businessDetailsResultObserver(): CustomObserverResponse<Business> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Business> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Business?
                ) {
                    viewModel.businessToView.value = data
                    handleData()
                }
            })
    }

    private fun setUpRvSlider() {
        businessSliderAdapter = BusinessSliderAdapter(this)
        binding?.layoutBusinessSlider?.vpPictures?.adapter = businessSliderAdapter
        getSnapHelper()?.attachToRecyclerView(binding?.layoutBusinessSlider?.vpPictures)
        binding?.layoutBusinessSlider?.vpPictures?.setOnItemClickListener(this)
        viewModel.businessToView.value?.images?.let {
            businessSliderAdapter.submitItems(
                it
            )
        }
    }

    private fun setUpRvFields() {
        businessFieldAdapter = BusinessFieldAdapter(this)
        binding?.layoutBusinessDetails?.rvFields?.adapter = businessFieldAdapter
        viewModel.businessToView.value?.fields?.let {
            businessFieldAdapter.submitItems(
                it
            )
        }
    }

    private fun setUpRvExtraInfo() {
        businessExtraInfoAdapter = BusinessExtraInfoAdapter(this)
        binding?.layoutBusinessExtraInfo?.rvExtraInfo?.adapter = businessExtraInfoAdapter
        viewModel.businessToView.value?.properties?.map { ExtraInfo(name = it.name, id = it.id) }
            ?.let {
                businessExtraInfoAdapter.submitItems(
                    it
                )
            }
    }

    private fun setUpRvDocuments() {
        businessDocumentAdapter = BusinessDocumentAdapter(this)
        binding?.layoutBusinessDocuments?.rvDocuments?.adapter = businessDocumentAdapter
        businessDocumentAdapter.submitItems(
            arrayListOf(
                Media(),
                Media()
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {
            is Media -> {
                view?.let { ViewImageActivity.start(this, item.name ?: "", it) }
            }
        }
    }

    companion object {
        fun start(
            context: Context?,
            business: Business,
            businessId: Int = -1
        ) {
            val intent = Intent(context, BusinessDetailsActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS, business)
                putExtra(Constants.BundleData.BUSINESS_ID, businessId)
            }
            context?.startActivity(intent)
        }
    }

}