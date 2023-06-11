package com.technzone.minibursa.ui.core.faqs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.FaqsResponse
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.databinding.ActivityFaqsBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.core.faqs.adapter.FaqsRecyclerAdapter
import com.technzone.minibursa.utils.extensions.gone
import com.technzone.minibursa.utils.extensions.visible
import com.technzone.minibursa.utils.plus
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FaqsActivity : BaseBindingActivity<ActivityFaqsBinding>() {

    private val viewModel: FaqsViewModel by viewModels()
    lateinit var adapter: FaqsRecyclerAdapter
    private var faqsList = ArrayList<FaqsResponse>()
    private var searchList = ArrayList<FaqsResponse>()

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_faqs,
            hasToolbar = true
        )
        addToolbar(
            toolbarView = binding?.toolbarLayout?.toolbar,
            tvToolbarTitleView = binding?.toolbarLayout?.tvToolbarTitle,
            hasToolbar = true,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.help_center
        )
        init()
    }

    private fun init() {
        setUpListeners()
        setUpAdapter()
        initSearch()
        viewModel.getFAQs().observe(this, faqsObserver())
    }

    private fun faqsObserver(): CustomObserverResponse<ListWrapper<FaqsResponse>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<ListWrapper<FaqsResponse>> {

                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<FaqsResponse>?
                ) {
                    faqsList = data?.data as ArrayList<FaqsResponse>
                    adapter.submitItems(faqsList)
                    hideShowNoData()
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    hideShowNoData()
                }
            })
    }


    private fun setUpAdapter() {
        adapter = FaqsRecyclerAdapter(this)
        binding?.rvHelpCenter?.adapter = adapter
    }

    private fun setUpListeners() {

    }

    private fun initSearch() {
        if (binding?.etSearch?.text?.isEmpty() == true) {
            adapter.submitItems(faqsList)
        }
        disposable + binding?.etSearch?.textChangeEvents()
            ?.debounce(300, TimeUnit.MILLISECONDS)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                applySearch(it.text.toString())
            }
    }

    private fun applySearch(text: String) {
        if (text.isNotEmpty()) {
            adapter.clear()
            faqsList.filter { it.question.contains(text) }.let {
                adapter.submitItems(it)
            }
            hideShowNoData()
        } else {
            binding?.layoutNoData?.root?.gone()
            adapter.submitItems(faqsList)
        }
    }

    private fun hideShowNoData() {
        if (adapter.itemCount == 0) {
            binding?.layoutNoData?.root?.visible()
        } else {
            binding?.layoutNoData?.root?.gone()
        }
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, FaqsActivity::class.java)
            context?.startActivity(intent)
        }

    }

}