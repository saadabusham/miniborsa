package com.technzone.miniborsa.ui.investor.invistorroles.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.country.Country
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import com.technzone.miniborsa.databinding.FragmentCompleteInvistorRoleBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.general.GeneralActivity
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.investor.invistorroles.viewmodels.InvestorRoleViewModel
import com.technzone.miniborsa.utils.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.internal.toLongOrDefault

@AndroidEntryPoint
class CompleteInvestorRoleFragment : BaseBindingFragment<FragmentCompleteInvistorRoleBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorRoleViewModel by activityViewModels()

    lateinit var selectedCountriesAdapter: SelectedGeneralRecyclerAdapter
    lateinit var selectedSpecialisationAdapter: SelectedGeneralRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_complete_invistor_role

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true
        )
        setUpBinding()
        setUpListeners()
        setUpSelectedCountriesAdapter()
        setUpSelectedSpecialistAdapter()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnComplete?.setOnClickListener {

        }
        binding?.tvCountries?.setOnClickListener {
            viewModel.getCountries().observe(this, countriesResultObserver())
        }
        binding?.tvCategories?.setOnClickListener {
            viewModel.getCategories().observe(this, categoriesResultObserver())
        }
        binding?.edBudget?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.investmentPrice.postValue(
                    binding?.edBudget?.text.toString()
                        .toLongOrDefault(viewModel.defaultMinValue.toLong()).toInt()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
    }

    private fun setUpSelectedCountriesAdapter() {
        selectedCountriesAdapter = SelectedGeneralRecyclerAdapter(requireContext())
        binding?.rvSelectedCountries?.adapter = selectedCountriesAdapter
        binding?.rvSelectedCountries?.setOnItemClickListener(this)
    }

    private fun countriesResultObserver(): CustomObserverResponse<List<Country>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<List<Country>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<Country>?
                ) {
                    if (!data.isNullOrEmpty())
                        data.map { GeneralLookup(id = it.id, name = it.name) }.let {
                            GeneralActivity.start(
                                requireContext(),
                                ArrayList(it),
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
                selectedCountriesAdapter.submitItems(data?.getSerializableExtra(Constants.BundleData.GENERAL_LIST) as List<GeneralLookup>)
            }
        }

    private fun setUpSelectedSpecialistAdapter() {
        selectedSpecialisationAdapter = SelectedGeneralRecyclerAdapter(requireContext())
        binding?.rvSelectedSpecialisation?.adapter = selectedSpecialisationAdapter
        binding?.rvSelectedSpecialisation?.setOnItemClickListener(this)
    }

    private fun categoriesResultObserver(): CustomObserverResponse<ListWrapper<CategoriesItem>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<CategoriesItem>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<CategoriesItem>?
                ) {
                    if (!data?.data.isNullOrEmpty())
                        data?.data?.map { GeneralLookup(id = it.id, name = it.name) }?.let {
                            GeneralActivity.start(
                                requireContext(),
                                ArrayList(it),
                                categoriesResultLauncher
                            )
                        }
                }
            })
    }

    var categoriesResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedSpecialisationAdapter.submitItems(data?.getSerializableExtra(Constants.BundleData.GENERAL_LIST) as List<GeneralLookup>)
            }
        }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as GeneralLookup
    }

}