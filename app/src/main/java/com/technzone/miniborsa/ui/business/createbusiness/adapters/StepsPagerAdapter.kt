package com.technzone.miniborsa.ui.business.createbusiness.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.technzone.miniborsa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.miniborsa.ui.business.createbusiness.fragments.CreateBusinessStep1Fragment
import com.technzone.miniborsa.ui.business.createbusiness.fragments.CreateBusinessStep2Fragment
import com.technzone.miniborsa.ui.business.createbusiness.fragments.CreateBusinessStep3Fragment
import com.technzone.miniborsa.ui.business.createbusiness.fragments.CreateBusinessStep4Fragment

class StepsPagerAdapter(fa: CreateBusinessActivity) : FragmentStateAdapter(fa) {
    private val createBusinessStep1Fragment = CreateBusinessStep1Fragment()
    private val createBusinessStep2Fragment = CreateBusinessStep2Fragment()
    private val createBusinessStep3Fragment = CreateBusinessStep3Fragment()
    private val createBusinessStep4Fragment = CreateBusinessStep4Fragment()

    companion object {
        private const val NUM_PAGES = 4
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> createBusinessStep1Fragment
            1 -> createBusinessStep2Fragment
            2 -> createBusinessStep3Fragment
            3 -> createBusinessStep4Fragment
            else -> Fragment()
        }
    }
}