package com.technzone.minibursa.ui.business.investors.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.technzone.minibursa.R
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.databinding.ActivityInvestorsBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorsActivity : BaseBindingActivity<ActivityInvestorsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_investors, hasToolbar = false)

        setStartDestination()
    }

    private fun setStartDestination() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.investors_nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.investors_nav_graph)

        if (intent.getIntExtra(Constants.BundleData.INVESTOR_ID,-1) == -1) {
            graph.setStartDestination(R.id.investorsFragment)
        } else {
            graph.setStartDestination(R.id.investorsFragment)
        }

        navHostFragment.navController.graph = graph
    }

    companion object {
        fun start(
            context: Context?,
            investorId:Int = -1
        ) {
            val intent = Intent(context, InvestorsActivity::class.java).apply {
                putExtra(Constants.BundleData.INVESTOR_ID,investorId)
            }
            context?.startActivity(intent)
        }
    }

}