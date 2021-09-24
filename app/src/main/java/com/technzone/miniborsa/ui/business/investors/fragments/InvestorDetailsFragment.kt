package com.technzone.miniborsa.ui.business.investors.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.databinding.FragmentInvestorDetailsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.investors.viewmodels.InvestorsViewModel
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessExtraInfoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorDetailsFragment : BaseBindingFragment<FragmentInvestorDetailsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorsViewModel by activityViewModels()

    private lateinit var countriesAdapter: BusinessExtraInfoAdapter
    private lateinit var categoriesAdapter: BusinessExtraInfoAdapter
    override fun getLayoutId(): Int = R.layout.fragment_investor_details

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        initData()
    }


    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initData() {
        if (viewModel.investorId != null) {
            loadData()
        } else if(viewModel.investorToView?.value != null) {
            setData()
        }
    }

    private fun setData() {
        setUpRvCountries()
        setUpRvCategories()
    }

    private fun loadData() {
        viewModel.investorId?.let { viewModel.getInvestorById(it).observe(this, investorDetailsResultObserver()) }
    }

    private fun setUpRvCountries() {
        countriesAdapter = BusinessExtraInfoAdapter(requireContext())
        binding?.rvCountries?.adapter = countriesAdapter
        viewModel.investorToView?.value?.countries?.map { ExtraInfo(name = it?.name, id = it?.id) }
            ?.let {
                countriesAdapter.submitItems(
                    it
                )
            }
    }


    private fun setUpRvCategories() {
        categoriesAdapter = BusinessExtraInfoAdapter(requireContext())
        binding?.rvCategories?.adapter = categoriesAdapter
        viewModel.investorToView?.value?.categories?.map { ExtraInfo(name = it?.name, id = it?.id) }
            ?.let {
                countriesAdapter.submitItems(
                    it
                )
            }
    }


    private fun investorDetailsResultObserver(): CustomObserverResponse<Investor> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Investor> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Investor?
                ) {
                    viewModel.investorToView?.value = data
                    setData()
                }
            })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {


    }

}