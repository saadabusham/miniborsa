package com.technzone.miniborsa.ui.investor.businessdetails.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.data.models.investor.FieldsItem
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

    private fun setUpRvSlider() {
        businessSliderAdapter = BusinessSliderAdapter(this)
        binding?.layoutBusinessSlider?.vpPictures?.adapter = businessSliderAdapter
        getSnapHelper()?.attachToRecyclerView(binding?.layoutBusinessSlider?.vpPictures)
        binding?.layoutBusinessSlider?.vpPictures?.setOnItemClickListener(this)
        businessSliderAdapter.submitItems(
            arrayListOf(
                Media(name = null),
                Media(name = null),
                Media(name = null),
                Media(name = null),
                Media(name = null),
                Media(name = null),
                Media(name = null),
                Media(name = null)
            )
        )
    }

    private fun setUpRvFields() {
        businessFieldAdapter = BusinessFieldAdapter(this)
        binding?.layoutBusinessDetails?.rvFields?.adapter = businessFieldAdapter
        businessFieldAdapter.submitItems(
            arrayListOf(
                FieldsItem(),
                FieldsItem(),
                FieldsItem(),
                FieldsItem(),
                FieldsItem(),
                FieldsItem(),
                FieldsItem(),
                FieldsItem(),
                FieldsItem(),
                FieldsItem()
            )
        )
    }

    private fun setUpRvExtraInfo() {
        businessExtraInfoAdapter = BusinessExtraInfoAdapter(this)
        binding?.layoutBusinessExtraInfo?.rvExtraInfo?.adapter = businessExtraInfoAdapter
        businessExtraInfoAdapter.submitItems(
            arrayListOf(
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo()
            )
        )
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
            business: Business
        ) {
            val intent = Intent(context, BusinessDetailsActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS, business)
            }
            context?.startActivity(intent)
        }
    }

}