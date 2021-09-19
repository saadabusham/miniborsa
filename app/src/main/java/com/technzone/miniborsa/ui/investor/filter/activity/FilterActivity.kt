package com.technzone.miniborsa.ui.investor.filter.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.interfaces.LoginCallBack
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.databinding.ActivityFilterBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.investor.filter.viewmodels.FilterBusinessViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterActivity : BaseBindingActivity<ActivityFilterBinding>() {

    var loginCallBack: LoginCallBack? = null
    private val viewModel: FilterBusinessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getIntExtra(Constants.BundleData.BUSINESS_TYPE, -1).let {
            if (it != -1)
                viewModel.selectedBusinessType = it
        }
        setContentView(R.layout.activity_filter, hasToolbar = false)
        postponeEnterTransition()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.getBooleanExtra(Constants.BundleData.IS_LOGIN_SUCCESS, false) == true)
            loginCallBack?.loggedInSuccess()
    }

    companion object {

        fun start(context: Activity, view: View, type: Int?) {
            val p1: androidx.core.util.Pair<View, String> = androidx.core.util.Pair(
                view,
                view.transitionName
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context,
                p1
            )
            val intent = Intent(context, FilterActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS_TYPE, type)
            }
            context.startActivity(intent, options.toBundle())
        }

    }

}