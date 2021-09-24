package com.technzone.miniborsa.ui.investor.news.news

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.databinding.FragmentNewsDetailsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.getFullUrl
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.businessdetails.adapters.BusinessExtraInfoAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.NewsAdapter
import com.technzone.miniborsa.ui.investor.news.viewmodels.NewsViewModel
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
        try {
            val dynamicLink: Task<ShortDynamicLink> =
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://www.minibursa.com/?${Constants.NEWS_ID}=" + viewModel.blogToView?.value?.id))
                    .setDomainUriPrefix("https://minibursa.page.link")
                    .setAndroidParameters(
                        DynamicLink.AndroidParameters.Builder("com.technzone.minibursa")
                            .setFallbackUrl(Uri.parse("www.minibursa.com"))
                            .build()
                    )
                    .setIosParameters(
                        DynamicLink.IosParameters.Builder("com.technzone.minibursa")
                            .setAppStoreId("123456789")
                            .setFallbackUrl(Uri.parse("www.minibursa.com"))
                            .build()
                    )
                    .setSocialMetaTagParameters(
                        DynamicLink.SocialMetaTagParameters.Builder()
                            .setTitle(
                                resources.getString(R.string.news_on_mini_bursa)
                            )
                            .setDescription(viewModel.blogToView?.value?.title ?: "")
                            .setImageUrl(
                                Uri.parse(
                                    getFullUrl(
                                        viewModel.blogToView?.value?.image ?: ""
                                    )
                                )
                            )
                            .build()
                    )
                    .buildShortDynamicLink()
            dynamicLink.addOnCompleteListener { task ->
                if (task.isSuccessful)
                    share(
                        task.result?.shortLink.toString()
                    )
            }.toString()
        } catch (e: IllegalStateException) {

        }
    }

    fun share(link: String) {
        try {
            if (viewModel.blogToView?.value == null) return
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                resources.getString(R.string.app_name)
            )
            var shareMessage =
                "\n${
                    resources.getString(R.string.share_this_news)
                } ${viewModel.blogToView?.value?.title} ${
                    resources.getString(R.string.share_on_mini_bursa)
                }\n\n"
            shareMessage = "$shareMessage$link\n\n"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            requireActivity().startActivity(Intent.createChooser(shareIntent, null))
        } catch (e: Exception) {
            //e.toString();
        }
    }


    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}