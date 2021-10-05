package com.technzone.miniborsa.ui.investor.invistormain.fragment.search

import android.content.SharedPreferences
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.interfaces.LoginCallBack
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.api.response.ResponseWrapper
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.databinding.FragmentInvestorSearchBinding
import com.technzone.miniborsa.ui.auth.AuthActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.dialogs.DialogsUtil.showLoginDialog
import com.technzone.miniborsa.ui.base.dialogs.LoginDialog
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.businessdraft.activity.BusinessDraftActivity
import com.technzone.miniborsa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.miniborsa.ui.investor.businessdetails.activity.BusinessDetailsActivity
import com.technzone.miniborsa.ui.investor.filter.activity.FilterActivity
import com.technzone.miniborsa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.miniborsa.ui.investor.invistormain.adapters.BusinessAdapter
import com.technzone.miniborsa.ui.investor.invistormain.adapters.BusinessNewsAdapter
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.FavoritesViewModel
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import com.technzone.miniborsa.ui.investor.news.activity.NewsActivity
import com.technzone.miniborsa.ui.subscription.activity.SubscriptionActivity
import com.technzone.miniborsa.utils.extensions.getSnapHelper
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InvestorSearchFragment : BaseBindingFragment<FragmentInvestorSearchBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener, LoginCallBack {

    private val viewModel: InvestorMainViewModel by activityViewModels()
    private val favoriteViewModel: FavoritesViewModel by activityViewModels()

    lateinit var forSaleBusinessAdapter: BusinessAdapter
    lateinit var shareForSaleBusinessAdapter: BusinessAdapter
    lateinit var franchiseBusinessAdapter: BusinessAdapter
    lateinit var businessNewsAdapter: BusinessNewsAdapter

    var isFullScreen: Boolean = false

    private var prefsChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun onResume() {
        super.onResume()
        handleStickyBusiness()
        loadAllData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_investor_search

    override fun onViewVisible() {
        super.onViewVisible()
        (requireActivity() as InvestorMainActivity).loginCallBack =
            this
        setUpBinding()
        setUpListeners()
        setUpRvForSaleBusiness()
        setUpRvShareForSaleBusiness()
        setUpRvFranchiseBusiness()
        setUpRvBusinessNews()
        handleNewNotifications()
        initPreferences()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.layoutForSale?.tvAll?.setOnClickListener {
            handleForSaleSelected(true)
        }
        binding?.layoutShareForSale?.tvAll?.setOnClickListener {
            handleShareForSaleSelected(true)
        }
        binding?.layoutFranchise?.tvAll?.setOnClickListener {
            handleFranchiseSelected(true)
        }
        binding?.layoutBusinessNews?.tvAll?.setOnClickListener {
            handleBusinessNews(true)
        }
        binding?.cvSearch?.setOnClickListener {
            FilterActivity.start(requireActivity(), binding?.cvSearch!!, null)
        }
        binding?.layoutSwitchBusiness?.layoutListBusiness?.btnListBusiness?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutListBusiness?.root?.gone()
            SubscriptionActivity.start(
                requireContext(),
                isBusiness = true,
                hasBusiness = false
            )
        }
        binding?.layoutSwitchBusiness?.layoutCompleteListing?.btnCompleteListing?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutCompleteListing?.root?.gone()
            BusinessDraftActivity.start(requireContext())
        }
        binding?.layoutSwitchBusiness?.layoutCompleteListing?.imgClose?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutCompleteListing?.root?.gone()
        }
        binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.btnSwitchToBusiness?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.root?.gone()
            viewModel.setCurrentUserRoles(UserRoleEnums.BUSINESS_ROLE.value)
            BusinessMainActivity.start(requireContext())
        }
        binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.imgClose?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.root?.gone()
        }
        binding?.imgNotifications?.setOnClickListener {
            navigationController.navigate(
                R.id.action_nav_search_to_notificationFragment,
                bundleOf(Pair(Constants.BundleData.SHOW_BACK, true))
            )
        }
    }

    private fun loadAllData() {
        loadBusinessForSale()
        loadBusinessShareForSale()
        loadBusinessFranchise()
    }

    private fun handleStickyBusiness() {
        if (!viewModel.isInvestor())
            return
        when {
            viewModel.isBusinessOwner() -> {
                binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.root?.visible()
            }
            else -> {
                viewModel.getPendingListing()
                    .observe(this, pendingResultObserver())
            }
        }
    }

    //    ForSale
    private fun setUpRvForSaleBusiness() {
        forSaleBusinessAdapter = BusinessAdapter(requireContext())
        binding?.layoutForSale?.recyclerView?.adapter = forSaleBusinessAdapter
        binding?.layoutForSale?.recyclerView.setOnItemClickListener(this)
        getSnapHelper()
            ?.attachToRecyclerView(binding?.layoutForSale?.recyclerView)
    }

    private fun handleForSaleSelected(fullScreen: Boolean) {
//        viewFullScreen(fullScreen)
//        loadBusinessForSale()
        FilterActivity.start(
            requireActivity(),
            binding?.cvSearch!!,
            BusinessTypeEnums.BUSINESS_FOR_SALE.value
        )
    }

    private fun loadBusinessForSale() {
        forSaleBusinessAdapter.clear()
        viewModel.getBusiness(BusinessTypeEnums.BUSINESS_FOR_SALE.value)
            .observe(this, forSaleResultObserver())
    }

    private fun forSaleResultObserver(): CustomObserverResponse<ListWrapper<Business>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<Business>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<Business>?
                ) {
                    if (data?.data != null && data.data.size > 0) {
                        if (data.data.size == 1)
                            binding?.layoutForSale?.tvAll?.gone()
                        forSaleBusinessAdapter.submitItems(data.data)
                    } else {
                        binding?.layoutForSale?.linearRoot?.gone()
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    binding?.layoutForSale?.linearRoot?.gone()
                }
            })
    }


    //    ShareForSale
    private fun setUpRvShareForSaleBusiness() {
        shareForSaleBusinessAdapter = BusinessAdapter(requireContext())
        binding?.layoutShareForSale?.recyclerView?.adapter = shareForSaleBusinessAdapter
        binding?.layoutShareForSale?.recyclerView.setOnItemClickListener(this)
        getSnapHelper()
            ?.attachToRecyclerView(binding?.layoutShareForSale?.recyclerView)
    }

    private fun handleShareForSaleSelected(fullScreen: Boolean) {
//        viewFullScreen(fullScreen)
//        loadBusinessShareForSale()
        FilterActivity.start(
            requireActivity(),
            binding?.cvSearch!!,
            BusinessTypeEnums.BUSINESS_FOR_SHARE.value
        )
    }

    private fun loadBusinessShareForSale() {
        shareForSaleBusinessAdapter.clear()
        viewModel.getBusiness(BusinessTypeEnums.BUSINESS_FOR_SHARE.value)
            .observe(this, forShareResultObserver())
    }

    private fun forShareResultObserver(): CustomObserverResponse<ListWrapper<Business>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<Business>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<Business>?
                ) {
                    if (data?.data != null && data.data.size > 0) {
                        if (data.data.size == 1)
                            binding?.layoutShareForSale?.tvAll?.gone()
                        shareForSaleBusinessAdapter.submitItems(data.data)
                    } else {
                        binding?.layoutShareForSale?.linearRoot?.gone()
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    binding?.layoutForSale?.linearRoot?.gone()
                }
            })
    }

    //    ShareForSale
    private fun setUpRvFranchiseBusiness() {
        franchiseBusinessAdapter = BusinessAdapter(requireContext())
        binding?.layoutFranchise?.recyclerView?.adapter = franchiseBusinessAdapter
        binding?.layoutFranchise?.recyclerView.setOnItemClickListener(this)
        getSnapHelper()
            ?.attachToRecyclerView(binding?.layoutFranchise?.recyclerView)
    }

    private fun handleFranchiseSelected(fullScreen: Boolean) {
//        viewFullScreen(fullScreen)
//        loadBusinessFranchise()
        FilterActivity.start(
            requireActivity(),
            binding?.cvSearch!!,
            BusinessTypeEnums.BUSINESS_FRANCHISE.value
        )
    }

    private fun loadBusinessFranchise() {
        franchiseBusinessAdapter.clear()
        viewModel.getBusiness(BusinessTypeEnums.BUSINESS_FRANCHISE.value)
            .observe(this, franchiseResultObserver())
    }

    private fun franchiseResultObserver(): CustomObserverResponse<ListWrapper<Business>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<Business>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<Business>?
                ) {
                    if (data?.data != null && data.data.size > 0) {
                        if (data.data.size == 1)
                            binding?.layoutFranchise?.tvAll?.gone()
                        franchiseBusinessAdapter.submitItems(data.data)
                    } else {
                        binding?.layoutFranchise?.linearRoot?.gone()
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    binding?.layoutFranchise?.linearRoot?.gone()
                }
            })
    }


    //    Business News
    private fun setUpRvBusinessNews() {
        businessNewsAdapter = BusinessNewsAdapter(requireContext())
        binding?.layoutBusinessNews?.recyclerView?.adapter = businessNewsAdapter
        binding?.layoutBusinessNews?.recyclerView.setOnItemClickListener(this)
        getSnapHelper()
            ?.attachToRecyclerView(binding?.layoutBusinessNews?.recyclerView)
        loadBusinessNews()
    }

    private fun handleBusinessNews(fullScreen: Boolean) {
        NewsActivity.start(requireContext())
    }

    private fun loadBusinessNews() {
        viewModel.getBlogs()
            .observe(this, newsResultObserver())
    }

    private fun newsResultObserver(): CustomObserverResponse<ListWrapper<BusinessNews>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<BusinessNews>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<BusinessNews>?
                ) {
                    if (data?.data != null && data.data.size > 0) {
                        if (data.data.size == 1)
                            binding?.layoutBusinessNews?.tvAll?.gone()
                        businessNewsAdapter.submitItems(data.data)
                    } else {
                        binding?.layoutBusinessNews?.linearRoot?.gone()
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    binding?.layoutBusinessNews?.linearRoot?.gone()
                }
            })
    }


    private fun viewFullScreen(fullScreen: Boolean) {
        isFullScreen = fullScreen
        TransitionManager.beginDelayedTransition(binding?.root as ViewGroup)
    }

    private fun showLoginDialog() {
        requireActivity().showLoginDialog(object : LoginDialog.CallBack {
            override fun callBackLogin() {
                AuthActivity.startForResult(requireActivity(), true)
            }
        })
    }

    override fun loggedInSuccess() {

    }

    private fun handleNewNotifications() {
        if (viewModel.isNewNotification())
            binding?.imgNotifications?.setImageResource(R.drawable.ic_alerts_active)
        else
            binding?.imgNotifications?.setImageResource(R.drawable.ic_alerts_unactive)
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

    private fun wishListObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            requireActivity(),
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

    private fun pendingResultObserver(): CustomObserverResponse<OwnerBusiness> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<OwnerBusiness> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: OwnerBusiness?
                ) {
                    data?.let {
                        binding?.layoutSwitchBusiness?.layoutCompleteListing?.root?.visible()
                    } ?: also {
                        binding?.layoutSwitchBusiness?.layoutListBusiness?.root?.visible()
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    binding?.layoutSwitchBusiness?.layoutListBusiness?.root?.visible()
                }
            }, showError = false
        )
    }


    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {
            is Business -> {
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
                        BusinessDetailsActivity.start(requireContext(), item)
                    }
                } else {
                    showLoginDialog()
                }
            }
            is BusinessNews -> {
                NewsActivity.start(requireContext(), item.id)
            }
        }
    }

}