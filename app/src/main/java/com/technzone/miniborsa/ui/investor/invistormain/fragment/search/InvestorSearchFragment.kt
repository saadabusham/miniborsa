package com.technzone.miniborsa.ui.investor.invistormain.fragment.search

import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.TransitionInflater
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.databinding.FragmentInvestorSearchBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.businessdetails.activity.BusinessDetailsActivity
import com.technzone.miniborsa.ui.investor.filter.activity.FilterActivity
import com.technzone.miniborsa.ui.investor.invistormain.adapters.BusinessAdapter
import com.technzone.miniborsa.ui.investor.invistormain.adapters.BusinessNewsAdapter
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import com.technzone.miniborsa.utils.extensions.getSnapHelper
import com.technzone.miniborsa.utils.extensions.gone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorSearchFragment : BaseBindingFragment<FragmentInvestorSearchBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by activityViewModels()

    lateinit var forSaleBusinessAdapter: BusinessAdapter
    lateinit var shareForSaleBusinessAdapter: BusinessAdapter
    lateinit var franchiseBusinessAdapter: BusinessAdapter
    lateinit var businessNewsAdapter: BusinessNewsAdapter

    var isFullScreen: Boolean = false
    override fun getLayoutId(): Int = R.layout.fragment_investor_search

    override fun onViewVisible() {
        super.onViewVisible()

        setUpBinding()
        setUpListeners()
        setUpRvForSaleBusiness()
        setUpRvShareForSaleBusiness()
        setUpRvFranchiseBusiness()
        setUpRvBusinessNews()
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
            FilterActivity.start(requireActivity(),binding?.cvSearch!!)
        }
        binding?.layoutSwitchBusiness?.layoutListBusiness?.btnListBusiness?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutListBusiness?.root?.gone()
        }
        binding?.layoutSwitchBusiness?.layoutCompleteListing?.btnCompleteListing?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutCompleteListing?.root?.gone()
        }
        binding?.layoutSwitchBusiness?.layoutCompleteListing?.imgClose?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutCompleteListing?.root?.gone()
        }
        binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.btnSwitchToBusiness?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.root?.gone()
        }
        binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.imgClose?.setOnClickListener {
            binding?.layoutSwitchBusiness?.layoutSwitchToBusiness?.root?.gone()
        }
    }

    //    ForSale
    private fun setUpRvForSaleBusiness() {
        forSaleBusinessAdapter = BusinessAdapter(requireContext())
        binding?.layoutForSale?.recyclerView?.adapter = forSaleBusinessAdapter
        binding?.layoutForSale?.recyclerView.setOnItemClickListener(this)
        getSnapHelper()
            ?.attachToRecyclerView(binding?.layoutForSale?.recyclerView)
        loadBusinessForSale()
    }

    private fun handleForSaleSelected(fullScreen: Boolean) {
        viewFullScreen(fullScreen)
        loadBusinessForSale()
    }

    private fun loadBusinessForSale() {
        forSaleBusinessAdapter.submitItems(
            arrayListOf(
                Business(),
                Business(),
                Business(),
                Business(),
                Business(),
                Business(),
                Business()
            )
        )
    }

    //    ShareForSale
    private fun setUpRvShareForSaleBusiness() {
        shareForSaleBusinessAdapter = BusinessAdapter(requireContext())
        binding?.layoutShareForSale?.recyclerView?.adapter = shareForSaleBusinessAdapter
        binding?.layoutShareForSale?.recyclerView.setOnItemClickListener(this)
        getSnapHelper()
            ?.attachToRecyclerView(binding?.layoutShareForSale?.recyclerView)
        loadBusinessShareForSale()
    }

    private fun handleShareForSaleSelected(fullScreen: Boolean) {
        viewFullScreen(fullScreen)
        loadBusinessShareForSale()
    }

    private fun loadBusinessShareForSale() {
        shareForSaleBusinessAdapter.submitItems(
            arrayListOf(
                Business(),
                Business(),
                Business(),
                Business(),
                Business(),
                Business(),
                Business()
            )
        )
    }

    //    ShareForSale
    private fun setUpRvFranchiseBusiness() {
        franchiseBusinessAdapter = BusinessAdapter(requireContext())
        binding?.layoutFranchise?.recyclerView?.adapter = franchiseBusinessAdapter
        binding?.layoutFranchise?.recyclerView.setOnItemClickListener(this)
        getSnapHelper()
            ?.attachToRecyclerView(binding?.layoutFranchise?.recyclerView)
        loadBusinessFranchise()
    }

    private fun handleFranchiseSelected(fullScreen: Boolean) {
        viewFullScreen(fullScreen)
        loadBusinessFranchise()
    }

    private fun loadBusinessFranchise() {
        franchiseBusinessAdapter.submitItems(
            arrayListOf(
                Business(),
                Business(),
                Business(),
                Business(),
                Business(),
                Business(),
                Business()
            )
        )
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
        viewFullScreen(fullScreen)
        loadBusinessNews()
    }

    private fun loadBusinessNews() {
        businessNewsAdapter.submitItems(
            arrayListOf(
                BusinessNews(),
                BusinessNews(),
                BusinessNews(),
                BusinessNews(),
                BusinessNews(),
                BusinessNews(),
                BusinessNews()
            )
        )
    }

    private fun viewFullScreen(fullScreen: Boolean) {
        isFullScreen = fullScreen
        TransitionManager.beginDelayedTransition(binding?.root as ViewGroup)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {
            is Business -> {
                BusinessDetailsActivity.start(requireContext(), item)
            }
        }
    }

}