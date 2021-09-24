package com.technzone.miniborsa.ui.business.investors.fragments

import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.paginate.Paginate
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter
import com.technzone.miniborsa.databinding.FragmentInvestorsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.investors.adapters.InvestorsRecyclerAdapter
import com.technzone.miniborsa.ui.business.investors.dialogs.InvestorFilterBottomSheet
import com.technzone.miniborsa.ui.business.investors.viewmodels.InvestorsViewModel
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.setupClearButtonWithAction
import com.technzone.miniborsa.utils.extensions.visible
import com.technzone.miniborsa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class InvestorsFragment : BaseBindingFragment<FragmentInvestorsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorsViewModel by activityViewModels()

    private lateinit var investorsRecyclerAdapter: InvestorsRecyclerAdapter
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    override fun getLayoutId(): Int = R.layout.fragment_investors

    override fun onDestroy() {
        super.onDestroy()
        callback.isEnabled = false
    }

    private val callback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {

        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        observeLoading()
        setUpListeners()
        setUpRvInvestors()
        initSearch()
    }

    private fun loadData() {
        viewModel.getInvestors().observe(this, investorsResultObserver())
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack2?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding?.imgBack?.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding?.linTools as ViewGroup)
            binding?.linToolbar?.visible()
            binding?.cvSearch?.gone()
        }
        binding?.imgFilter?.setOnClickListener {
            showFilterSheet()
        }
        binding?.imgSearch?.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding?.linTools as ViewGroup)
            binding?.cvSearch?.visible()
            binding?.linToolbar?.gone()
        }
    }

    private fun showFilterSheet() {
        InvestorFilterBottomSheet(viewModel.investorFilter,
            object : InvestorFilterBottomSheet.InvestorsFilterCallBack {
                override fun callBack(investorFilter: InvestorFilter) {
                    viewModel.investorFilter = investorFilter
                    loadData()
                }
            }).show(childFragmentManager, "FilterSheet")
    }

    private fun setUpRvInvestors() {
        investorsRecyclerAdapter = InvestorsRecyclerAdapter(requireContext())
        binding?.rvInvestors?.adapter = investorsRecyclerAdapter
        binding?.rvInvestors?.setOnItemClickListener(this)
        Paginate.with(binding?.rvInvestors, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && investorsRecyclerAdapter.itemCount > 0 && !isFinished) {
                    viewModel.investorFilter.pageNumber++
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

    private fun initSearch() {
        binding?.edSearch?.setupClearButtonWithAction()
        viewModel.compositeDisposable + binding?.edSearch?.textChangeEvents()
            ?.skipInitialValue()
            ?.debounce(600, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                viewModel.investorFilter.name = it.text.toString()
                applyFilter()
            }
    }

    private fun applyFilter() {
        investorsRecyclerAdapter.clear()
        viewModel.investorFilter.pageNumber = 1
        loadData()
    }


    private fun investorsResultObserver(): CustomObserverResponse<ListWrapper<Investor>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<Investor>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<Investor>?
                ) {
                    isFinished =
                        data?.data?.size?.plus(investorsRecyclerAdapter.itemCount) ?: 0 >= data?.totalRows ?: 0

                    data?.data?.let {
                        if (viewModel.investorFilter.pageNumber == 1) {
                            investorsRecyclerAdapter.submitItems(it)
                        } else {
                            investorsRecyclerAdapter.addItems(it)
                        }
                    }
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onLoading() {
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

    private fun observeLoading() {
        loading.observe(this, Observer {
            if (it) {
                binding?.rvInvestors?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.rvInvestors?.visible()
            }
        })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Investor
        viewModel.investorToView?.value = item
        viewModel.investorId = item.id
        if (view?.id == R.id.btnViewProfile) {
            navigationController.navigate(R.id.action_investorsFragment_to_investorDetailsFragment)
        } else if (view?.id == R.id.btnMessage) {

        }

    }

}