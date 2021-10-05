package com.technzone.miniborsa.ui.investor.businessdetails.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.Media
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.databinding.ActivityBusinessDetailsBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.dataview.viewimage.ViewImageActivity
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessDocumentAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessExtraInfoAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessFieldAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessSliderAdapter
import com.technzone.miniborsa.ui.investor.businessdetails.viewmodels.BusinessDetailsViewModel
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.FavoritesViewModel
import com.technzone.miniborsa.ui.investor.invistorroles.activity.InvestorRolesActivity
import com.technzone.miniborsa.ui.subscription.activity.SubscriptionActivity
import com.technzone.miniborsa.utils.DeepLinkUtil.generateDeepLink
import com.technzone.miniborsa.utils.extensions.getSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_business_details_toolbar.*

@AndroidEntryPoint
class BusinessDetailsActivity : BaseBindingActivity<ActivityBusinessDetailsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: BusinessDetailsViewModel by viewModels()
    private val favoriteViewModel: FavoritesViewModel by viewModels()
    private lateinit var businessSliderAdapter: BusinessSliderAdapter
    private lateinit var businessFieldAdapter: BusinessFieldAdapter
    private lateinit var businessExtraInfoAdapter: BusinessExtraInfoAdapter
    private lateinit var businessCountriesAdapter: BusinessExtraInfoAdapter
    private lateinit var businessDocumentAdapter: BusinessDocumentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_business_details,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = false
        )
        setUpBinding()
        setUpListeners()
        initData()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
        binding?.layoutBusinessInformation?.viewModel = viewModel
        binding?.layoutBusinessPrice?.viewModel = viewModel
    }

    private fun initData() {
        intent.getIntExtra(Constants.BundleData.BUSINESS_ID, -1).let {
            if (it == -1) {
                (intent.getSerializableExtra(Constants.BundleData.BUSINESS) as Business).let {
//                    favoriteViewModel.businessToView.value = it
//                    viewModel.businessToView.value = it
                    it.id?.let { it1 ->
                        viewModel.getBusiness(it1).observe(this, businessDetailsResultObserver())
                    }
                }
//                handleData()
            } else
                viewModel.getBusiness(it).observe(this, businessDetailsResultObserver())
        }
    }

    private fun handleData() {
        setUpRvSlider()
        setUpRvFields()
        setUpRvExtraInfo()
        setUpRvCountries()
        setUpRvDocuments()
    }

    private fun setUpListeners() {
        binding?.btnConnect?.setOnClickListener {
            SubscriptionActivity.start(this, false)
        }
        binding?.btnBecomeInvestor?.setOnClickListener {
            InvestorRolesActivity.start(this)
        }
        binding?.toolbar?.imgFavorite?.setOnClickListener {
            viewModel.businessToView.value?.isFavorite =
                viewModel.businessToView.value?.isFavorite == false
            updateFavorite()
            if (viewModel.businessToView.value?.isFavorite == true) {
                favoriteViewModel.addToWishList(viewModel.businessToView.value?.id ?: 0)
                    .observe(this, wishListObserver())
            } else {
                favoriteViewModel.removeFromWishList(
                    viewModel.businessToView.value?.id
                        ?: 0
                ).observe(this, wishListObserver())
            }
        }
        binding?.toolbar?.imgShare?.setOnClickListener {
            generateLink()
        }
    }

    private fun generateLink() {
        this.generateDeepLink(
            key = Constants.DeepLink.BUSINESS_ID,
            value = viewModel.businessToView.value?.id.toString(),
            description = viewModel.businessToView.value?.title ?: "",
            image = viewModel.businessToView?.value?.icon ?: "",
            socialTag = resources.getString(R.string.business_on_mini_bursa),
            shareTitle = resources.getString(R.string.share_this_business),
            title = viewModel.businessToView?.value?.title ?: ""
        )
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

    private fun updateFavorite() {
        binding?.toolbar?.favorite = viewModel.businessToView.value?.isFavorite
    }

    private fun businessDetailsResultObserver(): CustomObserverResponse<Business> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Business> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Business?
                ) {
                    viewModel.businessToView.value = data
                    favoriteViewModel.businessToView.value = data
                    updateFavorite()
                    handleData()
                }
            })
    }

    private fun setUpRvSlider() {
        businessSliderAdapter = BusinessSliderAdapter(this)
        binding?.layoutBusinessSlider?.vpPictures?.adapter = businessSliderAdapter
        getSnapHelper()?.attachToRecyclerView(binding?.layoutBusinessSlider?.vpPictures)
        binding?.layoutBusinessSlider?.vpPictures?.setOnItemClickListener(this)
        if (viewModel.businessToView.value?.images.isNullOrEmpty())
            viewModel.businessToView.value?.images = mutableListOf()
        viewModel.businessToView.value?.images?.add(
            0,
            Media(name = viewModel.businessToView.value?.icon ?: "")
        )
        businessSliderAdapter.submitItems(
            viewModel.businessToView.value?.images ?: mutableListOf()
        )

    }

    private fun setUpRvFields() {
        businessFieldAdapter = BusinessFieldAdapter(this)
        binding?.layoutBusinessDetails?.rvFields?.adapter = businessFieldAdapter
        viewModel.businessToView.value?.let {
            businessFieldAdapter.clear()
            businessFieldAdapter.submitItem(
                GeneralLookup(name = getString(R.string.date_founded), desc = it.establishedYear)
            )
            if (!it.categories.isNullOrEmpty())
                businessFieldAdapter.submitItem(
                    GeneralLookup(
                        name = getString(R.string.primary_industry),
                        desc = it.categories[0].name
                    )
                )
            if (it.categories?.size ?: 0 > 1)
                businessFieldAdapter.submitItem(
                    GeneralLookup(
                        name = getString(R.string.secondry_industry),
                        desc = it.categories?.get(1)?.name
                    )
                )
            if (it.categories?.size ?: 0 > 2)
                businessFieldAdapter.submitItem(
                    GeneralLookup(
                        name = getString(R.string.secondry_industry),
                        desc = it.categories?.get(2)?.name
                    )
                )
            if (it.askingPrice != null && it.askingPrice > 0)
                businessFieldAdapter.submitItem(
                    GeneralLookup(
                        name = getString(R.string.asking_price),
                        desc = if (it.askingPriceNA == true) getString(R.string.na) else String.format(
                            getString(R.string.dollar_sign_concated),
                            it.askingPrice
                        )
                    )
                )
            if (it.askingPriceBoth != null && it.askingPriceBoth > 0)
                businessFieldAdapter.submitItem(
                    GeneralLookup(
                        name = getString(R.string.leasehold),
                        desc = if (it.askingPriceNABoth == true) getString(R.string.na) else String.format(
                            getString(R.string.dollar_sign_concated),
                            it.askingPriceBoth
                        )
                    )
                )

            businessFieldAdapter.submitItem(
                GeneralLookup(
                    name = getString(R.string.annual_net_profit),
                    desc = if (it.annualNetProfitNA == true) getString(R.string.na) else String.format(
                        getString(R.string.dollar_sign_concated),
                        it.annualNetProfit
                    )
                )
            )

            businessFieldAdapter.submitItem(
                GeneralLookup(
                    name = getString(R.string.annual_turnover),
                    desc = if (it.annualTurnoverNA == true) getString(R.string.na) else String.format(
                        getString(R.string.dollar_sign_concated),
                        it.annualTurnover
                    )
                )
            )
        }
    }

    private fun setUpRvExtraInfo() {
        businessExtraInfoAdapter = BusinessExtraInfoAdapter(this)
        binding?.layoutBusinessExtraInfo?.rvExtraInfo?.adapter = businessExtraInfoAdapter
        viewModel.businessToView.value?.properties?.map { ExtraInfo(name = it.name, id = it.id) }
            ?.let {
                businessExtraInfoAdapter.submitItems(
                    it
                )
            }
    }

    private fun setUpRvCountries() {
        businessCountriesAdapter = BusinessExtraInfoAdapter(this)
        binding?.layoutBusinessCountries?.rvCountries?.adapter = businessCountriesAdapter
        viewModel.businessToView.value?.countries?.map { ExtraInfo(name = it.name, id = it.id) }
            ?.let {
                businessCountriesAdapter.submitItems(
                    it
                )
            }
    }

    private fun setUpRvDocuments() {
        businessDocumentAdapter = BusinessDocumentAdapter(this)
        binding?.layoutBusinessDocuments?.rvDocuments?.adapter = businessDocumentAdapter
        businessDocumentAdapter.submitItems(
            arrayListOf(
                Media(),
                Media()
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {
            is Media -> {
                view?.let { ViewImageActivity.start(this, item.name ?: "", it) }
            }
        }
    }

    companion object {
        fun start(
            context: Context?,
            business: Business? = null,
            businessId: Int? = -1
        ) {
            val intent = Intent(context, BusinessDetailsActivity::class.java).apply {
                putExtra(Constants.BundleData.BUSINESS, business)
                putExtra(Constants.BundleData.BUSINESS_ID, businessId)
            }
            context?.startActivity(intent)
        }
    }

}