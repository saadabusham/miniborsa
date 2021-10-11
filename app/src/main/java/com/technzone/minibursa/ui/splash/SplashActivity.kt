package com.technzone.minibursa.ui.splash

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.technzone.minibursa.R
import com.technzone.minibursa.common.MyApplication
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.models.configuration.ConfigurationWrapperResponse
import com.technzone.minibursa.databinding.ActivitySplashBinding
import com.technzone.minibursa.ui.auth.AuthActivity
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.minibursa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.minibursa.ui.userrole.activity.UserRolesActivity
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
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
        checkDeepLink()
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
                    goToNextPage()
                }
            })
    }

    private fun goToNextPage() {
        if (!viewModel.isUserLoggedIn()) {
            AuthActivity.start(this)
        } else
            openMainActivity()

    }

    private fun openMainActivity() {
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

    private fun checkDeepLink() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                if (deepLink != null) {
                    if (deepLink.getQueryParameter(Constants.DeepLink.BUSINESS_ID) != null) {
                        MyApplication.instance.businessIdDeepLink =
                            deepLink.getQueryParameter(Constants.DeepLink.BUSINESS_ID) ?: ""
                    } else if (deepLink.getQueryParameter(Constants.DeepLink.NEWS_ID) != null) {
                        MyApplication.instance.newsIdDeepLink =
                            deepLink.getQueryParameter(Constants.DeepLink.NEWS_ID) ?: ""
                    }
                }
            }
            .addOnFailureListener(this) { e ->
                Log.w(
                    "",
                    "getDynamicLink:onFailure",
                    e
                )
            }
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }

    }

}