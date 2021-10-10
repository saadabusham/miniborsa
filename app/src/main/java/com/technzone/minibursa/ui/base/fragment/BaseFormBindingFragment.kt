package com.technzone.minibursa.ui.base.fragment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.ui.business.createbusiness.viewmodels.CreateBusinessViewModel

abstract class BaseFormBindingFragment<BINDING : ViewDataBinding> :
    BaseBindingFragment<BINDING>() {

    protected val viewModel: CreateBusinessViewModel by activityViewModels()

    abstract fun validateToMoveToNext(callback: (Boolean) -> (Unit))

    abstract fun calculatePercentage()

}