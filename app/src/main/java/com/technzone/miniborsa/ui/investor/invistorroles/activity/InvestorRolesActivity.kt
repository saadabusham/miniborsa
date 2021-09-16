package com.technzone.miniborsa.ui.investor.invistorroles.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.ActivityInvistorRoleBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorRolesActivity : BaseBindingActivity<ActivityInvistorRoleBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invistor_role, hasToolbar = false)
    }

    companion object {
        fun start(
                context: Context?
        ) {
            val intent = Intent(context, InvestorRolesActivity::class.java)
            context?.startActivity(intent)
        }
    }

}