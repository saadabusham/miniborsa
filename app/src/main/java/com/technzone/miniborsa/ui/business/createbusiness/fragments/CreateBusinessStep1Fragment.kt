package com.technzone.miniborsa.ui.business.createbusiness.fragments

import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep1Binding
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.viewmodels.CreateBusinessViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateBusinessStep1Fragment : BaseFormBindingFragment<FragmentCreateBusinessStep1Binding>() {

    private val viewModel:CreateBusinessViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step1

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpListeners() {

    }

    private fun setUpBinding(){
        binding?.viewModel = viewModel
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        callback(true)
    }

}