package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.app.Activity
import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.interfaces.SeekbarCallback
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.country.Country
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep2FranchiseBinding
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.general.GeneralActivity
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.utils.extensions.calculatePercentage
import com.technzone.miniborsa.utils.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.toLongOrDefault

@AndroidEntryPoint
class CreateBusinessStep2FranchiseFragment :
    BaseFormBindingFragment<FragmentCreateBusinessStep2FranchiseBinding>() {


    lateinit var selectedCountriesAdapter: SelectedGeneralRecyclerAdapter
    override fun getLayoutId(): Int = R.layout.fragment_create_business_step2_franchise

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpSelectedCountriesAdapter()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.tvCountries?.setOnClickListener {
            viewModel.getCountries().observe(this, countriesResultObserver())
        }
        binding?.seekBarFreeHoldAskingPrice?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.freeHoldAskingPrice.postValue(progress.toDouble())
            }
        })
        binding?.edFreeHoldAskingPrice?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.freeHoldAskingPrice.postValue(
                    binding?.edFreeHoldAskingPrice?.text.toString()
                        .toDoubleOrNull()?:viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
        binding?.seekBarNetProfit?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.netProfit.postValue(progress.toDouble())
            }
        })
        binding?.edNetProfit?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.netProfit.postValue(
                    binding?.edNetProfit?.text.toString()
                        .toDoubleOrNull()?:viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
        binding?.seekBarTurnover?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.turnOver.postValue(progress.toDouble())
            }
        })
        binding?.edTurnover?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.turnOver.postValue(
                    binding?.edTurnover?.text.toString()
                        .toDoubleOrNull()?:viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
    }

    override fun validateToMoveToNext(callback: (Boolean) -> Unit) {
        viewModel.updateRequestBusiness().observe(this, updateRequestResultObserver(callback))
    }

    private fun updateRequestResultObserver(
        callback: (Boolean) -> Unit
    ): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    callback(true)
                }
            })
    }

    private fun setUpSelectedCountriesAdapter() {
        selectedCountriesAdapter = SelectedGeneralRecyclerAdapter(requireContext())
        binding?.rvSelectedCountries?.adapter = selectedCountriesAdapter
        selectedCountriesAdapter.submitNewItems(viewModel.countries.map { GeneralLookup(id = it.id,name = it.name)})
    }

    private fun countriesResultObserver(): CustomObserverResponse<ListWrapper<Country>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<Country>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<Country>?
                ) {
                    if (!data?.data.isNullOrEmpty())
                        data?.data?.map { GeneralLookup(id = it.id, name = it.name) }.let { countries ->
                            selectedCountriesAdapter.getSelectedItems()
                                .let { selectedCountries ->
                                    countries?.forEach { count ->
                                        selectedCountries.singleOrNull { it.id == count.id }
                                            ?.let {
                                                count.selected = true
                                            }
                                    }
                                }
                            GeneralActivity.start(
                                requireContext(),
                                ArrayList( countries),
                                countiesResultLauncher
                            )
                        }
                }
            })
    }

    var countiesResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                (data?.getSerializableExtra(Constants.BundleData.GENERAL_LIST) as List<GeneralLookup>).let {
                    var list = it
                    if (it.isNullOrEmpty())
                        list = mutableListOf()
                    selectedCountriesAdapter.submitNewItems(list)
                    viewModel.countries = list.toMutableList()
                }
            }
        }

    override fun calculatePercentage() {
        viewModel.percentage.postValue(viewModel.buildBusinessRequest().calculatePercentage())
    }


}