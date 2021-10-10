package com.technzone.minibursa.ui.auth.forgetpassword.fragments

import com.technzone.minibursa.R
import com.technzone.minibursa.databinding.FragmentRecoveryPasswordSuccessBinding
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoveryPasswordSuccessFragment :
    BaseBindingFragment<FragmentRecoveryPasswordSuccessBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_recovery_password_success

    override fun onViewVisible() {
        super.onViewVisible()
        setUpViewsListeners()
    }

    private fun setUpViewsListeners() {
        binding?.btnLogin?.setOnClickListener {
            requireActivity().finish()
        }
    }
}