package com.technzone.miniborsa.ui.business.createbusiness.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.databinding.ActivityCreateBusinessBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.adapters.StepsPagerAdapter
import com.technzone.miniborsa.ui.business.createbusiness.viewmodels.CreateBusinessViewModel
import com.technzone.miniborsa.utils.extensions.findCurrentFragment
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
    private fun setUpBinding(){
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
        stepsPagerAdapter = StepsPagerAdapter(this)
        binding?.formsViewPager?.adapter = stepsPagerAdapter
        binding?.formsViewPager?.registerOnPageChangeCallback(pagerCallback)
        binding?.formsViewPager?.currentItem
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
                1 -> {
                    viewModel.percentage.postValue(25)
                }
                2 -> {
                    viewModel.percentage.postValue(50)
                }
                3 -> {
                    viewModel.percentage.postValue(75)
                }
                4 -> {
                    viewModel.percentage.postValue(100)
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

    private fun goToPrevious() {
        binding?.formsViewPager?.setCurrentItem(
            (binding?.formsViewPager?.currentItem
                ?: 1) - 1, true
        )
        binding?.btnNext?.text = resources.getString(R.string.next)
    }

    companion object {
        fun start(
            context: Context?,
            businessType: Int
        ) {
            val intent = Intent(context, CreateBusinessActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS_TYPE, businessType)
            }
            context?.startActivity(intent)
        }
    }
}