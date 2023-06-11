package com.technzone.minibursa.ui.business.createbusiness.fragments

import android.graphics.Color
import android.view.inputmethod.EditorInfo
import com.google.android.material.tabs.TabLayout
import com.technzone.minibursa.R
import com.technzone.minibursa.common.interfaces.SeekbarCallback
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.PropertyStatusEnums
import com.technzone.minibursa.databinding.FragmentCreateBusinessStep2ForSaleBinding
import com.technzone.minibursa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.minibursa.utils.TabLayoutUtils.enableTabs
import com.technzone.minibursa.utils.extensions.calculatePercentage
import com.technzone.minibursa.utils.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateBusinessStep2ForSaleFragment :
    BaseFormBindingFragment<FragmentCreateBusinessStep2ForSaleBinding>() {


    override fun getLayoutId(): Int = R.layout.fragment_create_business_step2_for_sale

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpTabLayout()
    }

    private fun setUpListeners() {
        binding?.checkboxPropertyStatus?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.propertyStatusNa.postValue(isChecked)
            binding?.tabLayout?.enableTabs(!isChecked)
            viewModel.freeholdAskingPriceOnRequest.postValue(true)
            viewModel.leaseHoldAskingPriceOnRequest.postValue(true)
            viewModel.turnoverOnRequest.postValue(true)
            viewModel.netProfitOnRequest.postValue(true)
            viewModel.propertyStatus.value = (PropertyStatusEnums.FREEHOLD)
            updateTap()
        }
        binding?.seekBarFreeHoldAskingPrice?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.freeHoldAskingPrice.postValue(progress.toDouble())
            }
        })
        binding?.edFreeHoldAskingPrice?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.freeHoldAskingPrice.postValue(
                    binding?.edFreeHoldAskingPrice?.text.toString()
                        .toDoubleOrNull() ?: viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
        binding?.seekBarLeaseHoldAskingPrice?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.leaseHoldAskingPrice.postValue(progress.toDouble())
            }
        })
        binding?.edLeaseHoldAskingPrice?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.leaseHoldAskingPrice.postValue(
                    binding?.edLeaseHoldAskingPrice?.text.toString()
                        .toDoubleOrNull() ?: viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }

        binding?.seekBarNetProfit?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.netProfit.postValue(progress.toDouble())
            }
        })
        binding?.edNetProfit?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.netProfit.postValue(
                    binding?.edNetProfit?.text.toString()
                        .toDoubleOrNull() ?: viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
        binding?.seekBarTurnover?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.turnOver.postValue(progress.toDouble())
            }
        })
        binding?.edTurnover?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.turnOver.postValue(
                    binding?.edTurnover?.text.toString()
                        .toDoubleOrNull() ?: viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpTabLayout() {
        binding?.tabLayout?.addTab(binding?.tabLayout!!.newTab(), 0, true)
        binding?.tabLayout?.addTab(binding?.tabLayout!!.newTab(), 1, false)
        binding?.tabLayout?.addTab(binding?.tabLayout!!.newTab(), 2, false)
        binding?.tabLayout?.setSelectedTabIndicatorColor(Color.parseColor("#00000000"))
        binding?.tabLayout?.setSelectedTabIndicatorHeight(0)
        binding?.tabLayout?.setTabTextColors(
            Color.parseColor("#80000000"),
            Color.parseColor("#ffffff")
        )
        binding?.tabLayout?.getTabAt(0)?.text = resources.getString(R.string.freehold)
        binding?.tabLayout?.getTabAt(1)?.text = resources.getString(R.string.leasehold)
        binding?.tabLayout?.getTabAt(2)?.text = resources.getString(R.string.both)

        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.propertyStatus.postValue(
                    when (tab.position) {
                        0 -> PropertyStatusEnums.FREEHOLD
                        1 -> PropertyStatusEnums.LEASEHOLD
                        else -> PropertyStatusEnums.BOTH
                    }
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        updateTap()

    }

    private fun updateTap(){
        when (viewModel.propertyStatus.value) {
            PropertyStatusEnums.FREEHOLD -> {
                binding?.tabLayout?.selectTab(binding?.tabLayout?.getTabAt(0))
            }
            PropertyStatusEnums.LEASEHOLD -> {
                binding?.tabLayout?.selectTab(binding?.tabLayout?.getTabAt(1))
            }
            PropertyStatusEnums.BOTH -> {
                binding?.tabLayout?.selectTab(binding?.tabLayout?.getTabAt(2))
            }
            else -> {}
        }
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        viewModel.updateRequestBusiness().observe(this, updateRequestResultObserver(callback))
    }

    private fun updateRequestResultObserver(
        callback: (Boolean) -> Unit
    ): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    callback(true)
                }
            })
    }

    override fun calculatePercentage() {
        viewModel.percentage.postValue(viewModel.buildBusinessRequest().calculatePercentage())
    }


}