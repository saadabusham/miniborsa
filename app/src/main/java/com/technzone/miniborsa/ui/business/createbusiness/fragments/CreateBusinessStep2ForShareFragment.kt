package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.view.inputmethod.EditorInfo
import android.widget.SeekBar
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.interfaces.SeekbarCallback
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep2ForShareBinding
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.utils.extensions.calculatePercentage
import com.technzone.miniborsa.utils.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toLongOrDefault


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
        binding?.seekBarFreeHoldAskingPrice?.setOnSeekBarChangeListener(object : SeekbarCallback{
            override fun onFromUserChange(progress: Int) {
                viewModel.freeHoldAskingPrice.postValue(progress)
            }
        })
        binding?.edFreeHoldAskingPrice?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.freeHoldAskingPrice.postValue(
                    binding?.edFreeHoldAskingPrice?.text.toString().toLongOrDefault(viewModel.defaultMinValue.toLong()).toInt()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
        binding?.seekBarNetProfit?.setOnSeekBarChangeListener(object : SeekbarCallback{
            override fun onFromUserChange(progress: Int) {
                viewModel.netProfit.postValue(progress)
            }
        })
        binding?.edNetProfit?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.netProfit.postValue(
                    binding?.edNetProfit?.text.toString()
                        .toLongOrDefault(viewModel.defaultMinValue.toLong()).toInt()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
        binding?.seekBarTurnover?.setOnSeekBarChangeListener(object : SeekbarCallback{
            override fun onFromUserChange(progress: Int) {
                viewModel.turnOver.postValue(progress)
            }
        })
        binding?.edTurnover?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.turnOver.postValue(
                    binding?.edTurnover?.text.toString().toLongOrDefault(viewModel.defaultMinValue.toLong()).toInt()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        viewModel.updateRequestBusiness().observe(this,updateRequestResultObserver(callback))
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