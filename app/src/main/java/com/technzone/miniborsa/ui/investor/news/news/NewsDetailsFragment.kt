package com.technzone.miniborsa.ui.investor.news.news

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.FragmentNewsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.news.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailsFragment : BaseBindingFragment<FragmentNewsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by activityViewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}