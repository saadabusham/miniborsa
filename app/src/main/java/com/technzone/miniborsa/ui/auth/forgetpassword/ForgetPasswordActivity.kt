package com.technzone.miniborsa.ui.auth.forgetpassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.ActivityForgetPasswordBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordActivity : BaseBindingActivity<ActivityForgetPasswordBinding>() {

    companion object {
        fun start(
                context: Context?
        ) {
            val intent = Intent(context, ForgetPasswordActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password, hasToolbar = false)
    }

}