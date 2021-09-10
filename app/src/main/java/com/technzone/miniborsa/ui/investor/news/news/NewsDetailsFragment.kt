package com.technzone.miniborsa.ui.investor.news.news

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.databinding.FragmentNewsDetailsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessExtraInfoAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.NewsAdapter
import com.technzone.miniborsa.ui.investor.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_news_toolbar.*

@AndroidEntryPoint
class NewsDetailsFragment : BaseBindingFragment<FragmentNewsDetailsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by activityViewModels()

    private lateinit var tagsAdapter: BusinessExtraInfoAdapter
    lateinit var newsRecyclerAdapter: NewsAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_news_details
    }

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
            hasToolbar = true,
            toolbarView = binding?.layoutToolbar?.toolbar,
            hasBackButton = true,
            showBackArrow = true
        )
        setUpBinding()
        setUpListeners()
        setUpRvExtraInfo()
        setUpRvNews()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun setUpRvExtraInfo() {
        tagsAdapter = BusinessExtraInfoAdapter(requireContext())
        binding?.rvExtraInfo?.adapter = tagsAdapter
        tagsAdapter.submitItems(
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

    private fun setUpRvNews() {
        newsRecyclerAdapter = NewsAdapter(requireContext())
        binding?.rvArticles?.adapter = newsRecyclerAdapter
        binding?.rvArticles.setOnItemClickListener(this)
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

    }

}