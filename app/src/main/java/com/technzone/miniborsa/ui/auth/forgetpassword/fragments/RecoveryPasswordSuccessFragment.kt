package com.technzone.miniborsa.ui.auth.forgetpassword.fragments

import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.FragmentRecoveryPasswordSuccessBinding
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
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