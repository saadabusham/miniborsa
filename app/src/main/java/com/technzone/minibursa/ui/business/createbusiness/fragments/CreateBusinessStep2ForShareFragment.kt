package com.technzone.minibursa.ui.business.createbusiness.fragments

import android.view.inputmethod.EditorInfo
import com.technzone.minibursa.R
import com.technzone.minibursa.common.interfaces.SeekbarCallback
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.databinding.FragmentCreateBusinessStep2ForShareBinding
import com.technzone.minibursa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.minibursa.utils.extensions.calculatePercentage
import com.technzone.minibursa.utils.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateBusinessStep2ForShareFragment :
    BaseFormBindingFragment<FragmentCreateBusinessStep2ForShareBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step2_for_share

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.seekBarFreeHoldAskingPrice?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.freeHoldAskingPrice.postValue(progress.toDouble())
            }
        })
        binding?.edFreeHoldAskingPrice?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.freeHoldAskingPrice.postValue(
                    binding?.edFreeHoldAskingPrice?.text.toString()
                        .toDoubleOrNull()?:viewModel.defaultMinValue.toDouble()
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
                        .toDoubleOrNull()?:viewModel.defaultMinValue.toDouble()
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
                        .toDoubleOrNull()?:viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
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