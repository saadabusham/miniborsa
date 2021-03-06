package com.technzone.minibursa.ui.business.createbusiness.activity

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.BusinessTypeEnums
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.databinding.ActivityCreateBusinessBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.minibursa.ui.business.createbusiness.adapters.StepsPagerAdapter
import com.technzone.minibursa.ui.business.createbusiness.viewmodels.CreateBusinessViewModel
import com.technzone.minibursa.ui.business.listingpreview.activity.ListingPreviewActivity
import com.technzone.minibursa.utils.extensions.findCurrentFragment
import com.technzone.minibursa.utils.extensions.gone
import com.technzone.minibursa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.textColor

@AndroidEntryPoint
class CreateBusinessActivity : BaseBindingActivity<ActivityCreateBusinessBinding>() {

    lateinit var stepsPagerAdapter: StepsPagerAdapter
    private val viewModel: CreateBusinessViewModel by viewModels()
    lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.businessType = intent.getIntExtra(
            Constants.BundleData.BUSINESS_TYPE,
            BusinessTypeEnums.BUSINESS_FOR_SALE.value
        )
        viewModel.hasBusiness = intent.getBooleanExtra(
            Constants.BundleData.HAS_BUSINESS, false
        )
        viewModel.companyDraft = intent.getBooleanExtra(
            Constants.BundleData.COMPANY_DRAFT, false
        )
        viewModel.businessDraft = intent.getBooleanExtra(
            Constants.BundleData.BUSINESS_DRAFT, false
        )
        intent.getSerializableExtra(Constants.BundleData.BUSINESS)?.let {
            it as OwnerBusiness
            viewModel.buildBusinessRequestFromBusiness(it)
        }
        setContentView(
            R.layout.activity_create_business,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            titleString = String.format(getString(R.string.steps_number), 1)
        )
        setUpBinding()
        observePercentage()
        setUpPager()
        setUpListeners()
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding?.formsViewPager?.currentItem ?: 0 > 0) {
                    goToPrevious()
                } else {
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(callback)
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnNext?.setOnClickListener {
            binding?.formsViewPager?.findCurrentFragment(supportFragmentManager).let {
                it as BaseFormBindingFragment<*>
                it.validateToMoveToNext {
                    if (it) {
                        handleMoveToNext()
                    }
                }
            }
        }
    }

    private fun observePercentage() {
        viewModel.percentage.observe(this, {
            when {
                it <= 25 -> {
                    binding?.progressBar?.progressTintList =
                        ColorStateList.valueOf(getColor(R.color.chart_color_2))
                    binding?.imgPercentBg?.setImageResource(R.drawable.ic_percent25_bg)
                    binding?.tvPercent?.textColor = getColor(R.color.chart_color_2)
                }
                it <= 50 -> {
                    binding?.progressBar?.progressTintList =
                        ColorStateList.valueOf(getColor(R.color.chart_color_3))
                    binding?.imgPercentBg?.setImageResource(R.drawable.ic_percent50_bg)
                    binding?.tvPercent?.textColor = getColor(R.color.chart_color_3)
                }
                it <= 75 -> {
                    binding?.progressBar?.progressTintList =
                        ColorStateList.valueOf(getColor(R.color.chart_color_4))
                    binding?.imgPercentBg?.setImageResource(R.drawable.ic_percent75_bg)
                    binding?.tvPercent?.textColor = getColor(R.color.chart_color_4)
                }
                it <= 100 -> {
                    binding?.progressBar?.progressTintList =
                        ColorStateList.valueOf(getColor(R.color.chart_color_5))
                    binding?.imgPercentBg?.setImageResource(R.drawable.ic_percent100_bg)
                    binding?.tvPercent?.textColor = getColor(R.color.chart_color_5)
                }
            }
        })
    }

    private fun setUpPager() {
        binding?.formsViewPager?.offscreenPageLimit = 4
        stepsPagerAdapter = StepsPagerAdapter(this, viewModel.businessType)
        binding?.formsViewPager?.adapter = stepsPagerAdapter
        binding?.formsViewPager?.registerOnPageChangeCallback(pagerCallback)
        binding?.formsViewPager?.isUserInputEnabled = false
    }

    private var pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateToolbarTitle(
                titleString = String.format(
                    getString(R.string.steps_number),
                    position + 1
                )
            )
            when (position) {
                0 -> {
                    binding?.tvTitle?.text = getString(R.string.build_your_listing)
                }
                1 -> {
                    binding?.tvTitle?.text = getString(R.string.listing_score)
                }
                2 -> {
                    binding?.tvTitle?.text = getString(R.string.listing_score)
                }
                3 -> {
                    binding?.tvTitle?.text = getString(R.string.listing_score)
                    showHeaderPercent()
                }
            }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    private fun handleMoveToNext() {
        binding?.formsViewPager?.currentItem?.let {
            if (it == 3) {
                getBusinessDetails()
            } else {
                it.plus(1).let { it1 ->
                    binding?.formsViewPager?.setCurrentItem(
                        it1,
                        true
                    )
                }
            }
        }
    }

    private fun getBusinessDetails() {
        if (viewModel.isHasBusiness())
            viewModel.getBusinessRequest().observe(this, businessResultObserver())
        else {
            viewModel.getCompanyRequest().observe(this, businessResultObserver())
        }
    }

    private fun businessResultObserver(): CustomObserverResponse<OwnerBusiness> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<OwnerBusiness> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: OwnerBusiness?
                ) {
                    ListingPreviewActivity.start(
                        context = this@CreateBusinessActivity,
                        business = data,
                        hasBusiness = viewModel.hasBusiness,
                        companyDraft = viewModel.companyDraft,
                        businessDraft = viewModel.businessDraft
                    )
                }
            })
    }

    private fun goToPrevious() {
        binding?.formsViewPager?.setCurrentItem(
            (binding?.formsViewPager?.currentItem
                ?: 1) - 1, true
        )
        binding?.btnNext?.text = resources.getString(R.string.next_step)
    }

    companion object {
        fun start(
            context: Activity?,
            businessType: Int,
            business: OwnerBusiness? = null,
            hasBusiness: Boolean = true,
            companyDraft: Boolean = false,
            businessDraft: Boolean = false,
            updateBusiness: Boolean = false,
            clearTask: Boolean = false,
            forResult: Boolean = false
        ) {
            val intent = Intent(context, CreateBusinessActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS_TYPE, businessType)
                putExtra(Constants.BundleData.BUSINESS, business)
                putExtra(Constants.BundleData.HAS_BUSINESS, hasBusiness)
                putExtra(Constants.BundleData.COMPANY_DRAFT, companyDraft)
                putExtra(Constants.BundleData.BUSINESS_DRAFT, businessDraft)
                putExtra(Constants.BundleData.UPDATE_BUSINESS, updateBusiness)
                if (clearTask)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            if (forResult) {
                context?.startActivityForResult(intent, 0)
            } else {
                context?.startActivity(intent)
            }
        }
    }

    fun hideHeaderPercent() {
        binding?.constraintHeaderPercent?.gone()
    }

    fun showHeaderPercent() {
        binding?.constraintHeaderPercent?.visible()
    }
}