package com.technzone.miniborsa.ui.business.businessmain.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.interfaces.BaseActivityCallback
import com.technzone.miniborsa.databinding.ActivityBusinessMainBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.business.businessmain.viewmodels.BusinessMainViewModel
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessMainActivity : BaseBindingActivity<ActivityBusinessMainBinding>(),
    BaseActivityCallback {

    private val viewModel: BusinessMainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_main, hasToolbar = false)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.main_nav_host_fragment)
        navController.saveState()
        binding?.bnvMain?.let {
            NavigationUI.setupWithNavController(
                it,
                navController
            )
            it.setOnNavigationItemReselectedListener {
                // Do Nothing To Disable ReLunch fragment when reClick on nav icon
            }
        }
    }


    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, BusinessMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

    override fun hideBottomSheet() {
        binding?.bnvMain?.gone()
    }

    override fun showBottomSheet() {
        binding?.bnvMain?.visible()
    }
}