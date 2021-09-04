package com.technzone.miniborsa.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.databinding.ActivityAuthBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseBindingActivity<ActivityAuthBinding>() {

    @Inject lateinit var prefs : UserPref
    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth, hasToolbar = false)

        setStartDestination()
    }

    private fun setStartDestination() {

        val navHostFragment = auth_nav_host_fragment as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.auth_nav_graph)

        if (prefs.getIsFirstOpen()) {
            graph.startDestination = R.id.languageFragment
        } else {
            graph.startDestination = R.id.loginFragment
        }

        navHostFragment.navController.graph = graph
    }
}