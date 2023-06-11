package com.technzone.minibursa.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.technzone.minibursa.R
import com.technzone.minibursa.data.pref.user.UserPref
import com.technzone.minibursa.databinding.ActivityAuthBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseBindingActivity<ActivityAuthBinding>() {

    @Inject lateinit var prefs : UserPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth, hasToolbar = false)

        setStartDestination()
    }

    private fun setStartDestination() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.auth_nav_graph)

        if (prefs.getIsFirstOpen()) {
            graph.setStartDestination(R.id.languageFragment)
        } else {
            graph.setStartDestination(R.id.loginFragment)
        }

        navHostFragment.navController.graph = graph
    }
    companion object {

        const val REQUEST_CODE = 2
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
        fun startForResult(
            context: Activity?,
            isActivityResult: Boolean
        ) {
//            val intent = Intent(context, AuthActivity::class.java)
//            intent.putExtra(Constants.BundleData.IS_ACTIVITY_RESULT, isActivityResult)
//            context?.startActivityForResult(intent, REQUEST_CODE)
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }
}