package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.view.View
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.business.business.PropertiesItem
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep4Binding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.adapters.ExtraInfoAdapter
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
                if (item.selected.value == true) {
                    viewModel.selectedItemsCount.value = viewModel.selectedItemsCount.value?.plus(1)
                    viewModel.properties.add(item.id)
                } else {
                    viewModel.properties.remove(item.id)
                    viewModel.selectedItemsCount.value =
                        viewModel.selectedItemsCount.value?.minus(1)
                }
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
                        data?.data?.map { ExtraInfo(id = it.id, name = it.name) }?.let { list ->
                            viewModel.properties.forEach { prop ->
                                list.singleOrNull { it.id == prop }?.let {
                                    it.selected.value = true
                                }
                            }
                            viewModel.selectedItemsCount.value = list.filter { it.selected.value == true }.size
                            businessExtraInfoAdapter.submitItems(list)
                        }
                }
            })
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        viewModel.updateRequestBusiness().observe(this,updateRequestResultObserver(callback))
    }

    private fun updateRequestResultObserver(
        callback: (Boolean) -> Unit
    ): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    callback(true)
                }
            })
    }


}