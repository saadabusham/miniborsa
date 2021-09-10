package com.technzone.miniborsa.ui.base.fragment

import androidx.databinding.ViewDataBinding

abstract class BaseFormBindingFragment<BINDING : ViewDataBinding> :
    BaseBindingFragment<BINDING>() {

    abstract fun validateToMoveToNext(callback: (Boolean) -> (Unit))
}