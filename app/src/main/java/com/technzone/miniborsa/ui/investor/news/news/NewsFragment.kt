package com.technzone.miniborsa.ui.investor.news.news

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.databinding.FragmentNewsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.invistormain.adapters.BusinessNewsAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.IndicatorImagesRecyclerAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.NewsAdapter
import com.technzone.miniborsa.ui.investor.news.viewmodels.NewsViewModel
import com.technzone.miniborsa.utils.extensions.getSnapHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseBindingFragment<FragmentNewsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by activityViewModels()
    lateinit var businessNewsAdapter: NewsAdapter
    lateinit var indicatorRecyclerAdapter: IndicatorImagesRecyclerAdapter
    private var indicatorPosition = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpRvBusinessNews()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    //    Business News
    private fun setUpRvBusinessNews() {
        businessNewsAdapter = NewsAdapter(requireContext())
        binding?.layoutBusinessNews?.recyclerView?.adapter = businessNewsAdapter
        binding?.layoutBusinessNews?.recyclerView.setOnItemClickListener(this)
        loadBusinessNews()
    }

    private fun handleBusinessNews(fullScreen: Boolean) {
        loadBusinessNews()
    }

    private fun loadBusinessNews() {
        businessNewsAdapter.submitItems(
            arrayListOf(
                BusinessNews(),
                BusinessNews(),
                BusinessNews(),
                BusinessNews(),
                BusinessNews(),
                BusinessNews(),
                BusinessNews()
            )
        )
        setUpIndicator()
    }

    private fun setUpIndicator() {
        indicatorRecyclerAdapter = IndicatorImagesRecyclerAdapter(requireContext())
        binding?.layoutBusinessNews?.recyclerViewImagesDot?.adapter = indicatorRecyclerAdapter
        indicatorRecyclerAdapter.submitItems(businessNewsAdapter.items.withIndex().map { it.index == 0 })
        binding?.layoutBusinessNews?.recyclerView?.registerOnPageChangeCallback(
            pagerCallback
        )
    }

    private var pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateIndicator(position)
        }
    }

    private fun updateIndicator(position: Int) {
        indicatorRecyclerAdapter.items[indicatorPosition] = false
        indicatorRecyclerAdapter.items[position] = true
        indicatorRecyclerAdapter.notifyDataSetChanged()
        indicatorPosition = position
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}