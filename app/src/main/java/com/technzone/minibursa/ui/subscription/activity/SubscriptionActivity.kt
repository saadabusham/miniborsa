package com.technzone.minibursa.ui.subscription.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.technzone.minibursa.R
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.databinding.ActivitySubscriptionBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_subscription.*

@AndroidEntryPoint
class SubscriptionActivity : BaseBindingActivity<ActivitySubscriptionBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription, hasToolbar = false)
        setStartDestination()
    }

    private fun setStartDestination() {

        val navHostFragment = subscription_nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.subscription_nav_graph)

        if (intent.getBooleanExtra(Constants.BundleData.BUSINESS, false)) {
            graph.startDestination = R.id.businessSubscriptionFragment
        } else {
            graph.startDestination = R.id.investorSubscriptionFragment
        }

        navHostFragment.navController.graph = graph
    }

    companion object {
        fun start(
            context: Context?,
            isBusiness: Boolean,
            hasBusiness:Boolean = false,
            clearTask: Boolean = false
        ) {
            val intent = Intent(context, SubscriptionActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS, isBusiness)
                putExtra(Constants.BundleData.HAS_BUSINESS, hasBusiness)
                putExtra(Constants.BundleData.CLEAR_TASK, clearTask)
            }
            context?.startActivity(intent)
        }
    }

}