package com.technzone.miniborsa.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.MyApplication
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.miniborsa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.miniborsa.databinding.ActivitySplashBinding
import com.technzone.miniborsa.ui.auth.AuthActivity
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.miniborsa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.miniborsa.ui.userrole.activity.UserRolesActivity
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseBindingActivity<ActivitySplashBinding>() {

    private val viewModel: SplashViewModel by viewModels { defaultViewModelProviderFactory }

    @Inject
    lateinit var myApp: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            layoutResID = R.layout.activity_splash,
            hasToolbar = false
        )

        SharedPreferencesUtil.getInstance(applicationContext).setIsNewNotifications(true)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getConfigurationData().observe(this, configurationResultObserver())
        }, 3000)

        RuntimeException("This is a RUNTIME EXCEPTION")
    }

    private fun configurationResultObserver(): CustomObserverResponse<ConfigurationWrapperResponse> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<ConfigurationWrapperResponse> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ConfigurationWrapperResponse?
                ) {
                    SharedPreferencesUtil.getInstance(this@SplashActivity)
                        .setConfigurationPreferences(data)
                    viewModel.getFavorites().observe(this@SplashActivity,favoriteIdsResultObserver())
                }
            })
    }

    private fun favoriteIdsResultObserver(): CustomObserverResponse<List<Int>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<Int>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<Int>?
                ) {
                    goToNextPage()
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    goToNextPage()
                }
            },showError = false)
    }

    private fun goToNextPage() {
        if (!viewModel.isUserLoggedIn()) {
            AuthActivity.start(this)
//            UserRolesActivity.start(this)
        } else
            openMainActivity(viewModel.getUser())

    }

    private fun openMainActivity(data: UserDetailsResponseModel?) {
        when (viewModel.getCurrentUserRoles()) {
            UserRoleEnums.GUEST_ROLE.value -> {
                UserRolesActivity.start(this)
            }
            UserRoleEnums.VISITOR_ROLE.value, UserRoleEnums.INVESTOR_ROLE.value -> {
                InvestorMainActivity.start(this)
            }
            UserRoleEnums.BUSINESS_ROLE.value -> {
                BusinessMainActivity.start(this)
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onNewIntent(intent: Intent?) {
        this.intent = intent
        super.onNewIntent(intent)
    }


    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }

    }

}