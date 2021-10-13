package com.technzone.minibursa.ui.business.investors.fragments

import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.investor.ExtraInfo
import com.technzone.minibursa.data.models.investor.investors.Investor
import com.technzone.minibursa.databinding.FragmentInvestorDetailsBinding
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.business.investors.viewmodels.InvestorsViewModel
import com.technzone.minibursa.ui.core.chat.ChatActivity
import com.technzone.minibursa.ui.investor.businessdetails.adapters.BusinessExtraInfoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorDetailsFragment : BaseBindingFragment<FragmentInvestorDetailsBinding>() {

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
        binding?.btnMessage?.setOnClickListener {
            viewModel.investorToView?.value?.id?.let { it1 ->
                viewModel.getChanelId(it1).observe(this, chanelIdObserver())
            }
        }
    }

    private fun initData() {
        if (viewModel.investorId != null) {
            loadData()
        } else if (viewModel.investorToView?.value != null) {
            setData()
        }
    }

    private fun setData() {
        setUpRvCountries()
        setUpRvCategories()
    }

    private fun loadData() {
        viewModel.investorId?.let {
            viewModel.getInvestorById(it).observe(this, investorDetailsResultObserver())
        }
    }

    private fun setUpRvCountries() {
        countriesAdapter = BusinessExtraInfoAdapter(requireContext())
        binding?.rvCountries?.adapter = countriesAdapter
        viewModel.investorToView?.value?.countries?.map { ExtraInfo(name = it.name, id = it.id) }
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
                categoriesAdapter.submitItems(
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

    private fun chanelIdObserver(): CustomObserverResponse<String> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<String> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: String?
                ) {
                    data?.let {
                        ChatActivity.start(requireContext(), channelId = it)
                    }
                }
            }
        )
    }

}