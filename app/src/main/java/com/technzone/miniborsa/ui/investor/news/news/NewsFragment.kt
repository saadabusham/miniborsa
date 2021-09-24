package com.technzone.miniborsa.ui.investor.news.news

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.NewsSectionEnums
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.news.BusinessNews
import com.technzone.miniborsa.databinding.FragmentNewsBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.news.adapters.BusinessNewsSliderAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.IndicatorImagesRecyclerAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.NewsAdapter
import com.technzone.miniborsa.ui.investor.news.adapters.TabNewsRecyclerAdapter
import com.technzone.miniborsa.ui.investor.news.viewmodels.NewsViewModel
import com.technzone.miniborsa.utils.extensions.gone
import com.technzone.miniborsa.utils.extensions.setupClearButtonWithAction
import com.technzone.miniborsa.utils.extensions.visible
import com.technzone.miniborsa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class NewsFragment : BaseBindingFragment<FragmentNewsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by activityViewModels()
    lateinit var businessNewsSliderAdapter: BusinessNewsSliderAdapter
    lateinit var indicatorRecyclerAdapter: IndicatorImagesRecyclerAdapter
    lateinit var tabNewsAdapter: TabNewsRecyclerAdapter
    lateinit var newsRecyclerAdapter: NewsAdapter
    private var indicatorPosition = 0
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false
    var pageNumber: Int = 1
    private var selectedSectionsEnum: Int? = NewsSectionEnums.ALL.value
    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        observeLoading()
        initSearch()
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
        viewModel.getBannerBlogs().observe(this, bannersNewsResultObserver())
    }


    private fun bannersNewsResultObserver(): CustomObserverResponse<ListWrapper<BusinessNews>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<BusinessNews>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<BusinessNews>?
                ) {
                    if (data?.data != null && data.data.size > 0)
                        data.data.let {
                            businessNewsSliderAdapter.submitItems(it)
                            setUpIndicator()
                        } else {
                        binding?.layoutBusinessNews?.linearRoot?.gone()
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    binding?.layoutBusinessNews?.linearRoot?.gone()
                }
            })
    }

    private fun setUpIndicator() {
        indicatorRecyclerAdapter = IndicatorImagesRecyclerAdapter(requireContext())
        binding?.layoutBusinessNews?.recyclerViewImagesDot?.adapter = indicatorRecyclerAdapter
        indicatorRecyclerAdapter.submitItems(
            businessNewsSliderAdapter.items.withIndex().map { it.index == 0 })
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
                GeneralLookup(name = getString(R.string.all), isSelected = MutableLiveData(true)),
                GeneralLookup(name = getString(R.string.startup_news)),
                GeneralLookup(name = getString(R.string.investment)),
                GeneralLookup(name = getString(R.string.tips))
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

    private fun observeLoading() {
        loading.observe(this, {
            if (it) {
                binding?.rvNews?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
                binding?.rvNews?.visible()
            }
        })
    }

    private fun initSearch() {
        binding?.etSearch?.setupClearButtonWithAction()
        viewModel.compositeDisposable + binding?.etSearch?.textChangeEvents()
            ?.skipInitialValue()
            ?.debounce(600, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                viewModel.searchText.value = it.text.toString()
                applyFilter()
            }
    }

    private fun applyFilter() {
        newsRecyclerAdapter.clear()
        pageNumber = 1
        loadNews()
    }

    private fun loadNews() {
        viewModel.getBlogs(pageNumber,selectedSectionsEnum).observe(this, newsResultObserver())
    }

    private fun newsResultObserver(): CustomObserverResponse<ListWrapper<BusinessNews>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<BusinessNews>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<BusinessNews>?
                ) {
                    isFinished =
                        data?.data?.size?.plus(newsRecyclerAdapter.itemCount) ?: 0 >= data?.totalRows ?: 0

                    data?.data?.let {
                        if (pageNumber == 1) {
                            newsRecyclerAdapter.submitItems(it)
                        } else {
                            newsRecyclerAdapter.addItems(it)
                        }
                    }
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onLoading() {
                    super.onLoading()
                    loading.postValue(true)
                }
            }, withProgress = false
        )
    }

    private fun hideShowNoData() {
//        if (investorsRecyclerAdapter.itemCount == 0) {
//            binding?.layoutNoPolicies?.linearNoResult?.visible()
//        } else {
//            binding?.layoutNoPolicies?.linearNoResult?.gone()
//        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {
            is GeneralLookup -> {
                binding?.rvTabs?.smoothScrollToPosition(position)
                selectedSectionsEnum = NewsSectionEnums.getSectionByValue(position)?.value
                pageNumber = 1
                newsRecyclerAdapter.clear()
                loadNews()
            }
            is BusinessNews -> {
                viewModel.blogId = item.id ?: -1
                navigationController.navigate(R.id.action_newsFragment_to_newsDetailsFragment)
            }
        }
    }

}