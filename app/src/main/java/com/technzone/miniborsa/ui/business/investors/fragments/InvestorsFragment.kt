package com.technzone.miniborsa.ui.business.investors.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.investor.investors.Investor
import com.technzone.miniborsa.databinding.FragmentInvestorsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.investors.adapters.InvestorsRecyclerAdapter
import com.technzone.miniborsa.ui.business.investors.viewmodels.InvestorsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorsFragment : BaseBindingFragment<FragmentInvestorsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorsViewModel by activityViewModels()

    private lateinit var investorsRecyclerAdapter: InvestorsRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_investors

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpRvInvestors()
        loadData()
    }

    private fun loadData() {
        viewModel.getInvestors().observe(this, investorsResultObserver())
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun setUpRvInvestors() {
        investorsRecyclerAdapter = InvestorsRecyclerAdapter(requireContext())
        binding?.rvInvestors?.adapter = investorsRecyclerAdapter
        binding?.rvInvestors?.setOnItemClickListener(this)
    }

    private fun investorsResultObserver(): CustomObserverResponse<List<Investor>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<List<Investor>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<Investor>?
                ) {
                    if (data != null) {
                        investorsRecyclerAdapter.submitItems(data)
                    }
                }
            })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as Investor

    }

}