package com.technzone.miniborsa.ui.investor.news.news

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.databinding.FragmentNewsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.news.adapters.IndicatorImagesRecyclerAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.BusinessNewsSliderAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.NewsAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.TabNewsRecyclerAdapter
import com.technzone.miniborsa.ui.investor.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseBindingFragment<FragmentNewsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by activityViewModels()
    lateinit var businessNewsSliderAdapter: BusinessNewsSliderAdapter
    lateinit var indicatorRecyclerAdapter: IndicatorImagesRecyclerAdapter
    lateinit var tabNewsAdapter: TabNewsRecyclerAdapter
    lateinit var newsRecyclerAdapter: NewsAdapter
    private var indicatorPosition = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpRvBusinessNews()
        setUpRvTabNews()
        setUpRvNews()
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
        businessNewsSliderAdapter = BusinessNewsSliderAdapter(requireContext())
        binding?.layoutBusinessNews?.recyclerView?.adapter = businessNewsSliderAdapter
        binding?.layoutBusinessNews?.recyclerView.setOnItemClickListener(this)
        loadBusinessNews()
    }

    private fun handleBusinessNews(fullScreen: Boolean) {
        loadBusinessNews()
    }

    private fun loadBusinessNews() {
        businessNewsSliderAdapter.submitItems(
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
        indicatorRecyclerAdapter.submitItems(businessNewsSliderAdapter.items.withIndex().map { it.index == 0 })
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

    //    Business News
    private fun setUpRvTabNews() {
        tabNewsAdapter = TabNewsRecyclerAdapter(requireActivity())
        binding?.rvTabs?.adapter = tabNewsAdapter
        binding?.rvTabs.setOnItemClickListener(this)
        loadTabNews()
    }

    private fun loadTabNews() {
        tabNewsAdapter.submitItems(
            arrayListOf(
                GeneralLookup(name = "All",isSelected = MutableLiveData(true)),
                GeneralLookup(name = "Startup  News"),
                GeneralLookup(name = "Investment"),
                GeneralLookup(name = "Tips")
            )
        )
        setUpIndicator()
    }

    private fun setUpRvNews() {
        newsRecyclerAdapter = NewsAdapter(requireContext())
        binding?.rvNews?.adapter = newsRecyclerAdapter
        binding?.rvNews.setOnItemClickListener(this)
        loadNews()
    }

    private fun loadNews() {
        newsRecyclerAdapter.submitItems(
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
    }


    override fun onItemClick(view: View?, position: Int, item: Any) {
        when(item){
            is GeneralLookup -> {
                binding?.rvTabs?.smoothScrollToPosition(position)
            }
            is BusinessNews -> {
                navigationController.navigate(R.id.action_newsFragment_to_newsDetailsFragment)
            }
        }
    }

}