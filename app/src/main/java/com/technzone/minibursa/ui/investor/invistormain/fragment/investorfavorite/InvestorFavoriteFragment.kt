package com.technzone.minibursa.ui.investor.invistormain.fragment.investorfavorite

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.paginate.Paginate
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.api.response.ResponseWrapper
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.Business
import com.technzone.minibursa.databinding.FragmentInvestorFavoriteBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.investor.businessdetails.activity.BusinessDetailsActivity
import com.technzone.minibursa.ui.investor.invistormain.fragment.investorfavorite.adapters.FavoritesAdapter
import com.technzone.minibursa.ui.investor.invistormain.viewmodels.FavoritesViewModel
import com.technzone.minibursa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import com.technzone.minibursa.utils.extensions.gone
import com.technzone.minibursa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorFavoriteFragment : BaseBindingFragment<FragmentInvestorFavoriteBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by activityViewModels()
    private val favoriteViewModel: FavoritesViewModel by activityViewModels()
    private var page: Int = 1
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    private lateinit var favoriteAdapter: FavoritesAdapter

    override fun getLayoutId(): Int = R.layout.fragment_investor_favorite

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        observeLoading()
        setUpRvFavorites()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun setUpRvFavorites() {
        favoriteAdapter = FavoritesAdapter(requireContext())
        binding?.recyclerView?.adapter = favoriteAdapter
        binding?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && favoriteAdapter.itemCount > 0 && !isFinished) {
                    page++
                    loadFavorites()
                }
            }

            override fun isLoading(): Boolean {
                return loading.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isFinished
            }

        })
            .setLoadingTriggerThreshold(1)
            .addLoadingListItem(false)
            .build()
        loadFavorites()
    }

    private fun loadFavorites() {
        favoriteViewModel.getFavorites(pageNumber = page).observe(this, favoritesResultObserver())
    }

    private fun favoritesResultObserver(): CustomObserverResponse<ListWrapper<Business>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<Business>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<Business>?
                ) {
                    isFinished =
                        data?.data?.size?.plus(favoriteAdapter.itemCount) ?: 0 >= data?.totalRows ?: 0
                    data?.data?.let {
                        if (page == 1) {
                            favoriteAdapter.submitItems(it)
                        } else {
                            favoriteAdapter.addItems(it)
                        }
                    }
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onLoading() {
                    super.onLoading()
                    loading.postValue(true)
                }
            }, withProgress = false,showError = false
        )
    }

    private fun hideShowNoData() {
        if (favoriteAdapter.itemCount == 0) {
            binding?.layoutNoData?.root?.visible()
        } else {
            binding?.layoutNoData?.root?.gone()
        }
    }

    private fun observeLoading() {
        loading.observe(this) {
            if (it) {
                binding?.recyclerView?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.recyclerView?.visible()
            }
        }
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

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Business
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
    }

}