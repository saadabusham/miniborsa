package com.technzone.minibursa.ui.investor.recentviewed.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.databinding.ActivityRecentviewBinding
import com.technzone.minibursa.ui.auth.AuthActivity
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.dialogs.DialogsUtil.showLoginDialog
import com.technzone.minibursa.ui.base.dialogs.LoginDialog
import com.technzone.minibursa.ui.investor.businessdetails.activity.BusinessDetailsActivity
import com.technzone.minibursa.ui.investor.invistormain.viewmodels.FavoritesViewModel
import com.technzone.minibursa.ui.investor.recentviewed.adapters.RecentViewAdapter
import com.technzone.minibursa.ui.investor.recentviewed.viewmodels.RecentViewViewModel
import com.technzone.minibursa.utils.extensions.setupClearButtonWithAction
import com.technzone.minibursa.utils.extensions.visible
import com.technzone.minibursa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class RecentViewActivity : BaseBindingActivity<ActivityRecentviewBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: RecentViewViewModel by viewModels()
    private val favoriteViewModel: FavoritesViewModel by viewModels()
    private lateinit var recentViewAdapter: RecentViewAdapter
    private val originalList: MutableList<Business> = mutableListOf()
    override fun onResume() {
        super.onResume()
        loadData()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_recentview,
            hasToolbar = false
        )
        setUpRecyclerView()
        setUpListeners()
        initSearch()
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpRecyclerView() {
        recentViewAdapter = RecentViewAdapter(this)
        binding?.recyclerView?.adapter = recentViewAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
    }

    private fun loadData(){
        viewModel.getSearchedBusiness().observe(this) {
            if (it.isNotEmpty()) {
                recentViewAdapter.submitNewItems(it)
                originalList.apply {
                    clear()
                    addAll(it)
                }
            } else {
                binding?.layoutNoData?.root?.visible()
            }
        }
    }
    private fun initSearch() {
        binding?.edSearch?.setupClearButtonWithAction()
        viewModel.compositeDisposable + binding?.edSearch?.textChangeEvents()
            ?.skipInitialValue()
            ?.debounce(400, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                if (it.text.isNotEmpty()) {
                    applyFilter(it.text.toString())
                } else {
                    applyFilter("")
                }
            }
    }

    private fun applyFilter(text: String) {
        recentViewAdapter.clear()
        if (text.isEmpty()) {
            recentViewAdapter.submitItems(originalList)
        } else {
            recentViewAdapter.submitItems(originalList.filter {
                (it.title?.toLowerCase(Locale.ROOT)?.contains(
                    text.toLowerCase(Locale.ROOT)
                ) == true)
            })
        }
    }

    private fun wishListObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ResponseWrapper<Any>?
                ) {

                }
            }, false, showError = false
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Business
        if (viewModel.isUserLoggedIn()) {
            if (view?.id == R.id.imgFavorite) {
                if (item.isFavorite == true) {
                    favoriteViewModel.addToWishList(item.id ?: 0)
                        .observe(this, wishListObserver())
                } else {
                    favoriteViewModel.removeFromWishList(
                        item.id
                            ?: 0
                    ).observe(this, wishListObserver())
                }
            } else {
                BusinessDetailsActivity.start(this, item)
            }
        } else {
            showLoginDialog()
        }
    }

    private fun showLoginDialog() {
        showLoginDialog(object : LoginDialog.CallBack {
            override fun callBackLogin() {
                AuthActivity.startForResult(this@RecentViewActivity, true)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.getBooleanExtra(Constants.BundleData.IS_LOGIN_SUCCESS, false) == true) {

        }
    }

    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, RecentViewActivity::class.java)
            context?.startActivity(intent)
        }
    }

}