package com.technzone.minibursa.ui.business.createbusiness.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.technzone.minibursa.data.enums.BusinessTypeEnums
import com.technzone.minibursa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.minibursa.ui.business.createbusiness.fragments.*

class StepsPagerAdapter(
    fa: CreateBusinessActivity,
    private val businessType: Int
) : FragmentStateAdapter(fa) {
    private val createBusinessStep1Fragment = CreateBusinessStep1Fragment()
    private val createBusinessStep2ForSaleFragment = CreateBusinessStep2ForSaleFragment()
    private val createBusinessStep2ForShareFragment = CreateBusinessStep2ForShareFragment()
    private val createBusinessStep2FranchiseFragment = CreateBusinessStep2FranchiseFragment()
    private val createBusinessStep3Fragment = CreateBusinessStep3Fragment()
    private val createBusinessStep4Fragment = CreateBusinessStep4Fragment()

    companion object {
        private const val NUM_PAGES = 5
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> createBusinessStep1Fragment
            1 -> when (businessType) {
                BusinessTypeEnums.BUSINESS_FOR_SALE.value -> createBusinessStep2ForSaleFragment
                BusinessTypeEnums.BUSINESS_FOR_SHARE.value -> createBusinessStep2ForShareFragment
                else -> createBusinessStep2FranchiseFragment
            }
            2 -> createBusinessStep3Fragment
            3 -> createBusinessStep4Fragment
            else -> Fragment()
        }
    }
}