package com.technzone.miniborsa.ui.subscription.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.ActivitySubscriptionBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionActivity : BaseBindingActivity<ActivitySubscriptionBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription, hasToolbar = false)
    }

    companion object {
        fun start(
                context: Context?
        ) {
            val intent = Intent(context, SubscriptionActivity::class.java)
            context?.startActivity(intent)
        }
    }

}