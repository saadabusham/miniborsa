package com.technzone.minibursa.ui.countrypicker.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.models.general.Countries
import com.technzone.minibursa.databinding.ActivityCountriesBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.countrypicker.adapter.CountriesRecyclerAdapter
import com.technzone.minibursa.ui.countrypicker.viewmodels.CountriesViewModel
import com.technzone.minibursa.utils.extensions.readRawJson
import com.technzone.minibursa.utils.extensions.setupClearButtonWithAction
import com.technzone.minibursa.utils.extensions.showErrorAlert
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*
import java.util.concurrent.TimeUnit
import com.technzone.minibursa.utils.plus

@AndroidEntryPoint
class CountriesPickerActivity : BaseBindingActivity<ActivityCountriesBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: CountriesViewModel by viewModels()

    private lateinit var countriesAdapter: CountriesRecyclerAdapter
    private val originalList: MutableList<Countries> = mutableListOf()
    var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_countries,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            titleString = intent.getStringExtra(Constants.BundleData.TITLE)
        )
        setUpRecyclerView()
        setUpListeners()
        initSearch()
    }

    private fun setUpListeners() {
    }

    private fun onDone() {
        countriesAdapter.getSelectedItem()?.let {
            val data = Intent()
            data.putExtra(
                Constants.BundleData.COUNTRY,
                it
            )
            setResult(RESULT_OK, data)
            finish()
        }?:also {
            showErrorAlert(
                intent.getStringExtra(Constants.BundleData.TITLE)
                    ?: "", getString(R.string.please_select_item)
            )
        }
    }

    private fun setUpRecyclerView() {
        countriesAdapter = CountriesRecyclerAdapter(this)
        binding?.recyclerView?.adapter = countriesAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        val localData: List<Countries> =
            readRawJson(this, R.raw.countries)
        intent.getSerializableExtra(Constants.BundleData.COUNTRY)?.let {selectedCountry ->
            selectedCountry as Countries
            localData.singleOrNull { it.code == selectedCountry.code }?.let {
                it.selected = true
            }
        }
        countriesAdapter.submitItems(localData)
        originalList.addAll(localData)
//        viewModel.getCountriesCode().observe(this, countriesCodeResultObserver())
    }

    private fun initSearch() {
        binding?.edTitle?.setupClearButtonWithAction()
        compositeDisposable + binding?.edTitle?.textChangeEvents()
            ?.skipInitialValue()
            ?.debounce(400, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                if (it.text.isNotEmpty()) {
                    applyFilter(it.text.toString())
                } else {
                    applyFilter("")
                }
            }
    }

    private fun applyFilter(text: String) {
        countriesAdapter.clear()
        if (text.isNullOrEmpty()) {
            countriesAdapter.submitItems(originalList)
        } else {
            countriesAdapter.submitItems(originalList.filter {
                (it.code?.toLowerCase(Locale.ROOT)?.contains(
                    text.toLowerCase(Locale.ROOT)
                ) == true) || (it.name?.toLowerCase(Locale.ROOT)?.contains(
                    text.toLowerCase(Locale.ROOT)
                ) == true) || (it.countryCode?.toLowerCase(Locale.ROOT)?.contains(
                    text.toLowerCase(Locale.ROOT)
                ) == true)
            })
        }
    }

    private fun countriesCodeResultObserver(): CustomObserverResponse<List<Countries>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<Countries>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<Countries>?
                ) {
                    data?.let {
                        countriesAdapter.submitItems(it)
                        originalList.addAll(it)
                    }
                }
            })
    }

    companion object {

        fun start(
            context: Activity?,
            title: String,
            actonTitle: String,
            selectedCountry:Countries?,
            resultLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, CountriesPickerActivity::class.java)
            intent.apply {
                putExtra(Constants.BundleData.TITLE, title)
                putExtra(Constants.BundleData.ACTION_TITLE, actonTitle)
                putExtra(Constants.BundleData.COUNTRY, selectedCountry)
            }
            resultLauncher.launch(intent)
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        onDone()
    }
}