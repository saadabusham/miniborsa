package com.technzone.miniborsa.ui.investor.businessdetails.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.databinding.ActivityBusinessDetailsBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessSliderAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.viewmodels.BusinessDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_business_details_toolbar.*

@AndroidEntryPoint
class BusinessDetailsActivity : BaseBindingActivity<ActivityBusinessDetailsBinding>() {

    private val viewModel: BusinessDetailsViewModel by viewModels()
    private lateinit var businessSliderAdapter: BusinessSliderAdapter
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
        setUpRvSlider()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpRvSlider() {
        businessSliderAdapter = BusinessSliderAdapter(this)
        binding?.layoutBusinessSlider?.vpPictures?.adapter = businessSliderAdapter
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