package com.technzone.miniborsa.ui.auth.onboarding

import androidx.viewpager2.widget.ViewPager2
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.auth.onboarding.OnBoardingItem
import com.technzone.miniborsa.databinding.FragmentOnBoardingBinding
import com.technzone.miniborsa.ui.auth.onboarding.adapters.IndecatorRecyclerAdapter
import com.technzone.miniborsa.ui.auth.onboarding.adapters.OnBoardingAdapter
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_on_boarding.*

@AndroidEntryPoint
class OnBoardingFragment : BaseBindingFragment<FragmentOnBoardingBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_on_boarding
    lateinit var indecatorRecyclerAdapter: IndecatorRecyclerAdapter
    private var indecatorPosition = 0
    override fun onViewVisible() {
        super.onViewVisible()
        setUpPager()
        setUpListeners()
    }
    private fun setUpListeners() {
//        binding?.ivClose?.setOnClickListener {
//            navigationController.navigate(R.id.action_onBoardingFragment_to_loginFragment)
//        }
        binding?.btnNextOnBoarding?.setOnClickListener {
            if (binding?.vpOnBoarding?.currentItem ?: 0 < 2) {
                binding?.vpOnBoarding?.setCurrentItem((binding?.vpOnBoarding?.currentItem ?: 0) + 1, true)
            }
//            navigationController.navigate(R.id.action_onBoardingFragment_to_loginFragment)
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
        binding?.vpOnBoarding?.adapter =
            OnBoardingAdapter(requireContext()).apply { submitItems(items.toList()) }
        binding?.vpOnBoarding?.registerOnPageChangeCallback(pagerCallback)

    }


    private var pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateIndicator(position)
            if (position == 2) {
                btnNextOnBoarding.text = getString(R.string.get_started)
            } else {
                btnNextOnBoarding.text = getString(R.string.next)
            }
        }
    }

    private fun updateIndicator(position: Int) {
        if (!::indecatorRecyclerAdapter.isInitialized) {
            return
        }
        indecatorRecyclerAdapter.items[indecatorPosition] = false
        indecatorRecyclerAdapter.items[position] = true
        indecatorRecyclerAdapter.notifyDataSetChanged()
        indecatorPosition = position
    }


}