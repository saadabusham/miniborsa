package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.view.View
import com.technzone.miniborsa.R
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
                if (item.selected.value == true)
                    viewModel.selectedItemsCount.value = viewModel.selectedItemsCount.value?.plus(1)
                else
                    viewModel.selectedItemsCount.value =
                        viewModel.selectedItemsCount.value?.minus(1)
            }
        })
        businessExtraInfoAdapter.submitItems(
            arrayListOf(
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo(),
                ExtraInfo()
            )
        )
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        callback(true)
    }

}