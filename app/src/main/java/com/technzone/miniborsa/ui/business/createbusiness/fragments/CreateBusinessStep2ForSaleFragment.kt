package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.graphics.Color
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.PropertyStatusEnums
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep2ForSaleBinding
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.viewmodels.CreateBusinessViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateBusinessStep2ForSaleFragment : BaseFormBindingFragment<FragmentCreateBusinessStep2ForSaleBinding>() {

    private val viewModel:CreateBusinessViewModel by activityViewModels()

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step2_for_sale

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpTabLayout()
    }

    private fun setUpListeners() {

    }

    private fun setUpBinding(){
        binding?.viewModel = viewModel
    }

    private fun setUpTabLayout() {
        binding?.tabLayout?.addTab(binding?.tabLayout!!.newTab(), 0, true)
        binding?.tabLayout?.addTab(binding?.tabLayout!!.newTab(), 1, false)
        binding?.tabLayout?.addTab(binding?.tabLayout!!.newTab(), 2, false)
        binding?.tabLayout?.setSelectedTabIndicatorColor(Color.parseColor("#00000000"))
        binding?.tabLayout?.setSelectedTabIndicatorHeight(0)
        binding?.tabLayout?.setTabTextColors(
            Color.parseColor("#80000000"),
            Color.parseColor("#ffffff")
        )
        binding?.tabLayout?.getTabAt(0)?.text = resources.getString(R.string.freehold)
        binding?.tabLayout?.getTabAt(1)?.text = resources.getString(R.string.leasehold)
        binding?.tabLayout?.getTabAt(2)?.text = resources.getString(R.string.both)

        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.propertyStatus.postValue(
                    when(tab.position){
                        0 -> PropertyStatusEnums.FREEHOLD
                        1 -> PropertyStatusEnums.LEASEHOLD
                        else -> PropertyStatusEnums.BOTH
                    }
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        callback(true)
    }

}