package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.country.Country
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.data.models.investor.PropertiesItem
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep4Binding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.adapters.ExtraInfoAdapter
import com.technzone.miniborsa.ui.general.GeneralActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateBusinessStep4Fragment : BaseFormBindingFragment<FragmentCreateBusinessStep4Binding>() {

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step4

    private lateinit var businessExtraInfoAdapter: ExtraInfoAdapter
    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpRvExtraInfo()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun setUpRvExtraInfo() {
        businessExtraInfoAdapter = ExtraInfoAdapter(requireActivity())
        binding?.rvExtraInfo?.adapter = businessExtraInfoAdapter
        binding?.rvExtraInfo?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                item as ExtraInfo
                if (item.selected.value == true)
                    viewModel.selectedItemsCount.value = viewModel.selectedItemsCount.value?.plus(1)
                else
                    viewModel.selectedItemsCount.value =
                        viewModel.selectedItemsCount.value?.minus(1)
            }
        })
        viewModel.getProperties().observe(this, propertiesResultObserver())
    }

    private fun propertiesResultObserver(): CustomObserverResponse<ListWrapper<PropertiesItem>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<PropertiesItem>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<PropertiesItem>?
                ) {
                    if (!data?.data.isNullOrEmpty())
                        data?.data?.map { ExtraInfo(id = it.id, name = it.name) }?.let {
                            businessExtraInfoAdapter.submitItems(it)
                        }
                }
            })
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        callback(true)
    }

}