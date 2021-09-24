package com.technzone.miniborsa.ui.investor.filter.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.paginate.Paginate
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.interfaces.LoginCallBack
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.databinding.FragmentSearchBusinessBinding
import com.technzone.miniborsa.ui.auth.AuthActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.dialogs.DialogsUtil.showLoginDialog
import com.technzone.miniborsa.ui.base.dialogs.LoginDialog
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.businessdetails.activity.BusinessDetailsActivity
import com.technzone.miniborsa.ui.investor.filter.activity.FilterActivity
import com.technzone.miniborsa.ui.investor.filter.viewmodels.FilterBusinessViewModel
import com.technzone.miniborsa.ui.investor.invistormain.adapters.BusinessAdapter
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.setupClearButtonWithAction
import com.technzone.miniborsa.utils.extensions.visible
import com.technzone.miniborsa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchBusinessFragment : BaseBindingFragment<FragmentSearchBusinessBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener, LoginCallBack {

    private val viewModel: FilterBusinessViewModel by activityViewModels()
    lateinit var businessAdapter: BusinessAdapter
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    var firstIn: Boolean = true

    override fun onDestroy() {
        super.onDestroy()
        callback.isEnabled = false
    }

    private val callback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_business
    }

    override fun onResume() {
        super.onResume()
        if (firstIn && viewModel.selectedBusinessType == null) {
            loadData()
        } else {
            applyFilter()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition: Transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition
    }

    override fun onViewVisible() {
        super.onViewVisible()

        if (viewModel.selectedBusinessType == null) {
            binding?.linToolbar?.gone()
            binding?.cvSearch?.visible()
        } else {
            binding?.linToolbar?.visible()
            binding?.cvSearch?.gone()
        }

        (requireActivity() as FilterActivity).loginCallBack =
            this
        binding?.cvSearch?.viewTreeObserver?.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding?.cvSearch?.viewTreeObserver?.removeOnPreDrawListener(this)
                requireActivity().startPostponedEnterTransition()
                return true
            }
        })
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        setUpBinding()
        observeLoading()
        setUpListeners()
        setUpRvBusiness()
        initSearch()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            if (viewModel.selectedBusinessType == null) {
                requireActivity().onBackPressed()
            } else {
                TransitionManager.beginDelayedTransition(binding?.linTools as ViewGroup)
                binding?.linToolbar?.visible()
                binding?.cvSearch?.gone()
            }
        }
        binding?.imgBack2?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding?.tvFilter?.setOnClickListener {
            navigationController.navigate(R.id.action_searchBusinessFragment_to_filterBusinessFragment)
        }
        binding?.imgSearch?.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding?.linTools as ViewGroup)
            binding?.cvSearch?.visible()
            binding?.linToolbar?.gone()
        }
    }

    private fun setUpRvBusiness() {
        businessAdapter = BusinessAdapter(requireContext())
        binding?.recyclerView?.adapter = businessAdapter
        binding?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && businessAdapter.itemCount > 0 && !isFinished) {
                    viewModel.pageNumber++
                    loadData()
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
    }

    private fun observeLoading() {
        loading.observe(this, {
            if (it) {
                binding?.recyclerView?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.recyclerView?.visible()
            }
        })
    }

    private fun initSearch() {
        binding?.edSearch?.setupClearButtonWithAction()
        viewModel.compositeDisposable + binding?.edSearch?.textChangeEvents()
            ?.skipInitialValue()
            ?.debounce(600, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                viewModel.searchText.value = it.text.toString()
                applyFilter()
            }
    }

    private fun applyFilter() {
        businessAdapter.clear()
        viewModel.pageNumber = 1
        loadData()
    }

    private fun loadData() {
        viewModel.getBusiness().observe(this, businessResultObserver())
    }

    private fun businessResultObserver(): CustomObserverResponse<ListWrapper<Business>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<Business>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<Business>?
                ) {
                    isFinished =
                        data?.data?.size?.plus(businessAdapter.itemCount) ?: 0 >= data?.totalRows ?: 0

                    data?.data?.let {
                        viewModel.itemFoundCount.value = data.data.size
                        if (viewModel.pageNumber == 1) {
                            businessAdapter.submitItems(it)
                        } else {
                            businessAdapter.addItems(it)
                        }
                    } ?: also {
                        viewModel.itemFoundCount.value = 0
                    }
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    viewModel.itemFoundCount.value = 0
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onLoading() {
                    super.onLoading()
                    loading.postValue(true)
                }
            }, withProgress = false
        )
    }

    private fun hideShowNoData() {
//        if (investorsRecyclerAdapter.itemCount == 0) {
//            binding?.layoutNoPolicies?.linearNoResult?.visible()
//        } else {
//            binding?.layoutNoPolicies?.linearNoResult?.gone()
//        }
    }

    private fun showLoginDialog() {
        requireActivity().showLoginDialog(object : LoginDialog.CallBack {
            override fun callBackLogin() {
                AuthActivity.startForResult(requireActivity(), true)
            }
        })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {
            is Business -> {
                if (viewModel.isUserLoggedIn()) {
                    viewModel.saveSearchBusiness(item)
                    BusinessDetailsActivity.start(requireContext(), item)
                } else {
                    showLoginDialog()
                }
            }
        }
    }

    override fun loggedInSuccess() {

    }


}