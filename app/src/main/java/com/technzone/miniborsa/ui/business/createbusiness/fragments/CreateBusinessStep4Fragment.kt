package com.technzone.miniborsa.ui.business.createbusiness.fragments

import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep1Binding
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep2Binding
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep4Binding
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.viewmodels.CreateBusinessViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateBusinessStep4Fragment : BaseFormBindingFragment<FragmentCreateBusinessStep4Binding>() {

    private val viewModel:CreateBusinessViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step4

    override fun onViewVisible() {
        super.onViewVisible()
        setUpListeners()
    }

    private fun setUpListeners() {

    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        callback(true)
    }

}