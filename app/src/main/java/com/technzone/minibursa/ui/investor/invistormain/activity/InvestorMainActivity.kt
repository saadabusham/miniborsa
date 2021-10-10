package com.technzone.minibursa.ui.investor.invistormain.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.technzone.minibursa.R
import com.technzone.minibursa.common.MyApplication
import com.technzone.minibursa.common.interfaces.BaseActivityCallback
import com.technzone.minibursa.common.interfaces.LoginCallBack
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.databinding.ActivityInvestorMainBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.investor.businessdetails.activity.BusinessDetailsActivity
import com.technzone.minibursa.ui.investor.invistormain.viewmodels.FavoritesViewModel
import com.technzone.minibursa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import com.technzone.minibursa.ui.investor.news.activity.NewsActivity
import com.technzone.minibursa.utils.extensions.gone
import com.technzone.minibursa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorMainActivity : BaseBindingActivity<ActivityInvestorMainBinding>(),
    BaseActivityCallback {

    var loginCallBack: LoginCallBack? = null
    private val viewModel: InvestorMainViewModel by viewModels()
    private val favoriteViewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_investor_main, hasToolbar = false)
        setupNavigation()
        favoriteViewModel.getFavoritesIds().observe(this, favoriteIdsResultObserver())
        checkDeepLink()
    }

    private fun checkDeepLink() {
        if (MyApplication.instance.newsIdDeepLink.isNotEmpty()) {
            val id = MyApplication.instance.newsIdDeepLink.toIntOrNull()
            MyApplication.instance.newsIdDeepLink = ""
            NewsActivity.start(this, id)
        } else if (MyApplication.instance.businessIdDeepLink.isNotEmpty()) {
            val id = MyApplication.instance.businessIdDeepLink.toIntOrNull()
            MyApplication.instance.businessIdDeepLink = ""
            BusinessDetailsActivity.start(this, businessId = id)
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

    override fun hideBottomSheet() {
        binding?.bnvMain?.gone()
    }

    override fun showBottomSheet() {
        binding?.bnvMain?.visible()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.getBooleanExtra(Constants.BundleData.IS_LOGIN_SUCCESS, false) == true)
            loginCallBack?.loggedInSuccess()
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
                    data?.let { favoriteViewModel.setFavoriteList(it) }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                }
            }, showError = false
        )
    }

    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, InvestorMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }
}