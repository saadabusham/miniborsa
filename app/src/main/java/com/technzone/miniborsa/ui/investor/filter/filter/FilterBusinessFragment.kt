package com.technzone.miniborsa.ui.investor.filter.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import com.technzone.miniborsa.databinding.FragmentFilterBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.filter.filter.adapters.GeneralLookupRecyclerAdapter
import com.technzone.miniborsa.ui.investor.filter.viewmodels.FilterBusinessViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterBusinessFragment : BaseBindingFragment<FragmentFilterBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: FilterBusinessViewModel by activityViewModels()
    private lateinit var businessTypeAdapter: GeneralLookupRecyclerAdapter
    private lateinit var inustryAdapter: GeneralLookupRecyclerAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_filter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition: Transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpRvIndustries()
        setUpRvBusinessType()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding?.rangeSeekBar?.setOnRangeSeekbarChangeListener { minValue: Number, maxValue: Number ->
//            filter.minPrice = minValue.toFloat()
//            filter.maxPrice = maxValue.toFloat()
//            tvMin.setText(minValue.toString() + " " + getString(R.string.jd));
//            tvMax.setText(maxValue.toString() + " " + getString(R.string.jd));
            binding?.tvMin?.text = String.format(getString(R.string.price_), minValue.toString())
            binding?.tvMax?.text = String.format(getString(R.string.price_), maxValue.toString())
        }
        binding?.rangeSeekBar?.setMinValue(100000f)?.apply()
        binding?.rangeSeekBar?.setMaxValue(100000000f)?.apply()
    }

    private fun setUpRvIndustries() {
        inustryAdapter = GeneralLookupRecyclerAdapter(requireContext())
        binding?.rvIndustry?.adapter = inustryAdapter
        binding?.rvIndustry?.setOnItemClickListener(this)
        binding?.rvIndustry?.itemAnimator = null
        viewModel.getCategories().observe(this, categoriesResultObserver())
    }

    private fun categoriesResultObserver(): CustomObserverResponse<ListWrapper<CategoriesItem>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<CategoriesItem>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<CategoriesItem>?
                ) {
                    if (!data?.data.isNullOrEmpty())
                        data?.data?.map { GeneralLookup(id = it.id, name = it.name) }?.let {
                            inustryAdapter.submitItems(it)
                        }
                }
            })
    }

    private fun setUpRvBusinessType() {
        businessTypeAdapter = GeneralLookupRecyclerAdapter(requireContext())
        binding?.rvBusinessType?.adapter = businessTypeAdapter
        binding?.rvBusinessType?.setOnItemClickListener(object : BaseBindingRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, item: Any) {
//                viewModel.selectedBusinessType = businessTypeAdapter.getSelectedItem()?.id
            }
        })
        binding?.rvBusinessType?.itemAnimator = null
        businessTypeAdapter.submitItems(
            arrayListOf(
                GeneralLookup(
                    name = getString(R.string.businesses_for_sale),
                    id = BusinessTypeEnums.BUSINESS_FOR_SALE.value,
                    selected = viewModel.selectedBusinessType == BusinessTypeEnums.BUSINESS_FOR_SALE.value
                ),
                GeneralLookup(
                    name = getString(R.string.businesses_for_sale),
                    id = BusinessTypeEnums.BUSINESS_FOR_SHARE.value,
                    selected = viewModel.selectedBusinessType == BusinessTypeEnums.BUSINESS_FOR_SHARE.value
                ),
                GeneralLookup(
                    name = getString(R.string.franchise),
                    id = BusinessTypeEnums.BUSINESS_FRANCHISE.value,
                    selected = viewModel.selectedBusinessType == BusinessTypeEnums.BUSINESS_FRANCHISE.value
                )
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}