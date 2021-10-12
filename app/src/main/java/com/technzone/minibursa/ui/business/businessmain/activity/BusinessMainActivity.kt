package com.technzone.minibursa.ui.business.businessmain.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.MenuItemCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.technzone.minibursa.R
import com.technzone.minibursa.common.interfaces.BaseActivityCallback
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.databinding.ActivityBusinessMainBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.business.businessmain.viewmodels.BusinessMainViewModel
import com.technzone.minibursa.ui.subscription.activity.BusinessSubscriptionActivity
import com.technzone.minibursa.utils.extensions.gone
import com.technzone.minibursa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BusinessMainActivity : BaseBindingActivity<ActivityBusinessMainBinding>(),
    BaseActivityCallback {

    private var prefsChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private val viewModel: BusinessMainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_main, hasToolbar = false)
        setupNavigation()
        handleNewNotifications()
        initPreferences()
        BusinessSubscriptionActivity.start(this,0,subscriptionResultLauncher)
    }
    var subscriptionResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            }
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
    private fun handleNewNotifications(){
        if (viewModel.isNewNotification()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                binding?.bnvMain?.menu?.getItem(1)?.isCheckable = false
                binding?.bnvMain?.menu?.getItem(1)?.let {
                    MenuItemCompat.setIconTintMode(it, PorterDuff.Mode.DST)
                }
            }
            binding?.bnvMain?.menu?.getItem(1)?.icon = getDrawable(R.drawable.ic_alerts_active)
        }
        else
            binding?.bnvMain?.menu?.getItem(1)?.icon = getDrawable(R.drawable.ic_nav_notification)
    }

    private fun initPreferences() {
        prefsChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                Constants.NotificationsChannels.NEW_NOTIFICATIONS -> {
                    handleNewNotifications()
                }
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(prefsChangeListener)
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