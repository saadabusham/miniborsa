package com.technzone.minibursa.ui.auth.onboarding

import androidx.viewpager2.widget.ViewPager2
import com.technzone.minibursa.R
import com.technzone.minibursa.data.models.auth.onboarding.OnBoardingItem
import com.technzone.minibursa.databinding.FragmentOnBoardingBinding
import com.technzone.minibursa.ui.auth.onboarding.adapters.IndecatorRecyclerAdapter
import com.technzone.minibursa.ui.auth.onboarding.adapters.OnBoardingAdapter
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.utils.extensions.invisible
import com.technzone.minibursa.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_on_boarding.*

@AndroidEntryPoint
class OnBoardingFragment : BaseBindingFragment<FragmentOnBoardingBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_on_boarding
    lateinit var onBoardingAdapter: OnBoardingAdapter
    lateinit var indecatorRecyclerAdapter: IndecatorRecyclerAdapter
    private var indecatorPosition = 0
    override fun onViewVisible() {
        super.onViewVisible()
        setUpPager()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.ivClose?.setOnClickListener {
            navigationController.navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }
        binding?.btnNextOnBoarding?.setOnClickListener {
            if (binding?.vpOnBoarding?.currentItem ?: 0 < 2) {
                binding?.vpOnBoarding?.setCurrentItem(
                    (binding?.vpOnBoarding?.currentItem ?: 0) + 1,
                    true
                )
            } else
                navigationController.navigate(R.id.action_onBoardingFragment_to_loginFragment)
        }
    }

    private fun setUpPager() {
        val items = arrayOf(
            OnBoardingItem(
                icon = R.drawable.ic_on_boarding_1,
                title = R.string.title_on_boarding_1,
                body = R.string.description_on_boarding_1
            ),
            OnBoardingItem(
                icon = R.drawable.ic_on_boarding_2,
                title = R.string.title_on_boarding_2,
                body = R.string.description_on_boarding_2
            ),
            OnBoardingItem(
                icon = R.drawable.ic_on_boarding_3,
                title = R.string.title_on_boarding_3,
                body = R.string.description_on_boarding_3
            )
        )
        onBoardingAdapter = OnBoardingAdapter(requireContext()).apply { submitItems(items.toList()) }
        binding?.vpOnBoarding?.adapter = onBoardingAdapter
        setUpIndicator()
    }

    private fun setUpIndicator() {
        indecatorRecyclerAdapter = IndecatorRecyclerAdapter(requireContext())
        binding?.recyclerViewImagesDot?.adapter = indecatorRecyclerAdapter
        onBoardingAdapter.items.let {
            it.withIndex().forEach {
                indecatorRecyclerAdapter.submitItem(it.index == 0)
            }
        }
        binding?.vpOnBoarding?.registerOnPageChangeCallback(pagerCallback)
    }


    private var pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateIndicator(position)
            if (position == 2) {
                btnNextOnBoarding.text = getString(R.string.get_started)
                binding?.ivClose?.invisible()
            } else {
                btnNextOnBoarding.text = getString(R.string.next)
                binding?.ivClose?.visible()
            }
        }
    }

    private fun updateIndicator(position: Int) {
        indecatorRecyclerAdapter.items[indecatorPosition] = false
        indecatorRecyclerAdapter.items[position] = true
        indecatorRecyclerAdapter.notifyDataSetChanged()
        indecatorPosition = position
    }


}