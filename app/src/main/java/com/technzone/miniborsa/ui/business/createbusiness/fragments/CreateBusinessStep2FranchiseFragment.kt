package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.view.inputmethod.EditorInfo
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep2FranchiseBinding
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.utils.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toLongOrDefault

@AndroidEntryPoint
class CreateBusinessStep2FranchiseFragment : BaseFormBindingFragment<FragmentCreateBusinessStep2FranchiseBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step2_franchise

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding(){
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.edFreeHoldAskingPrice?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.freeHoldAskingPrice.postValue(
                    binding?.edFreeHoldAskingPrice?.text.toString().toLongOrDefault(viewModel.defaultMinValue.toLong()).toInt()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
        binding?.edNetProfit?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.netProfit.postValue(
                    binding?.edNetProfit?.text.toString().toLongOrDefault(viewModel.defaultMinValue.toLong()).toInt()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
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
        callback(true)
    }

}