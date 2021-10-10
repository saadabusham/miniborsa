package com.technzone.minibursa.ui.investor.news.news

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.investor.ExtraInfo
import com.technzone.minibursa.data.models.news.BusinessNews
import com.technzone.minibursa.databinding.FragmentNewsDetailsBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.dataview.viewimage.ViewImageActivity
import com.technzone.minibursa.ui.investor.businessdetails.adapters.BusinessExtraInfoAdapter
import com.technzone.minibursa.ui.investor.news.adapters.NewsAdapter
import com.technzone.minibursa.ui.investor.news.viewmodels.NewsViewModel
import com.technzone.minibursa.utils.DeepLinkUtil.generateDeepLink
import dagger.hilt.android.AndroidEntryPoint

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
//        setUpRvExtraInfo()
//        setUpRvNews()
        loadNewsDetails()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.layoutToolbar?.imgShare?.setOnClickListener {
            generateLink()
        }
        binding?.imgPhoto?.setOnClickListener {
            ViewImageActivity.start(requireActivity(), viewModel.blogToView?.value?.image ?: "", it)
        }
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

    private fun loadNewsDetails() {
        viewModel.getBlogDetails(viewModel.blogId).observe(this, newsDetailsResultObserver())
    }

    private fun newsDetailsResultObserver(): CustomObserverResponse<BusinessNews> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<BusinessNews> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: BusinessNews?
                ) {
                    viewModel.blogToView?.value = data
                }
            }
        )
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

    private fun generateLink() {
        requireContext().generateDeepLink(
            key = Constants.DeepLink.NEWS_ID,
            value = viewModel.blogToView?.value?.id.toString(),
            description = viewModel.blogToView?.value?.title ?: "",
            image = viewModel.blogToView?.value?.image ?: "",
            socialTag = resources.getString(R.string.news_on_mini_bursa),
            shareTitle = resources.getString(R.string.share_this_news),
            title = viewModel.blogToView?.value?.title ?: ""
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}