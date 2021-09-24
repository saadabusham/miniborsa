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
import com.technzone.miniborsa.databinding.FragmentBecomeInvistorBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.general.GeneralActivity
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.miniborsa.ui.investor.invistorroles.viewmodels.InvestorRoleViewModel
import com.technzone.miniborsa.utils.extensions.hideKeyboard
import com.technzone.miniborsa.utils.extensions.showErrorAlert
import com.technzone.miniborsa.utils.extensions.validate
import com.technzone.miniborsa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.internal.toLongOrDefault

@AndroidEntryPoint
class BecomeInvestorRoleFragment : BaseBindingFragment<FragmentBecomeInvistorBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorRoleViewModel by activityViewModels()

    lateinit var selectedCountriesAdapter: SelectedGeneralRecyclerAdapter
    lateinit var selectedSpecialisationAdapter: SelectedGeneralRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_become_invistor

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
            if (isDataValid()) {
                viewModel.becomeInvestor(
                    countries = selectedCountriesAdapter.getSelectedItems().map { it.id ?: 0 },
                    categories = selectedSpecialisationAdapter.getSelectedItems().map { it.id ?: 0 }
                ).observe(this, becomeInvestorResultObserver())
            }
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

    private fun isDataValid(): Boolean {
        binding?.edJobTitle?.text?.toString()?.validate(ValidatorInputTypesEnums.TEXT,requireContext())?.let {
            if(!it.isValid){
                requireActivity().showErrorAlert(getString(R.string.job_title),it.errorMessage)
                return false
            }
        }
        binding?.edBio?.text?.toString()?.validate(ValidatorInputTypesEnums.TEXT,requireContext())?.let {
            if(!it.isValid){
                requireActivity().showErrorAlert(getString(R.string.bio),it.errorMessage)
                return false
            }
        }
        if (selectedCountriesAdapter.getSelectedItems().isNullOrEmpty()) {
            requireActivity().showErrorAlert(
                getString(R.string.app_name),
                getString(R.string.please_select_your_countries)
            )
            return false
        }
        if (selectedSpecialisationAdapter.getSelectedItems().isNullOrEmpty()) {
            requireActivity().showErrorAlert(
                getString(R.string.app_name),
                getString(R.string.please_select_your_specialisations)
            )
            return false
        }
        return true
    }

    private fun setUpSelectedCountriesAdapter() {
        selectedCountriesAdapter = SelectedGeneralRecyclerAdapter(requireContext())
        binding?.rvSelectedCountries?.adapter = selectedCountriesAdapter
        binding?.rvSelectedCountries?.setOnItemClickListener(this)
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
                        data?.data?.map { GeneralLookup(id = it.id, name = it.name) }.let {
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
                selectedCountriesAdapter.submitNewItems(data?.getSerializableExtra(Constants.BundleData.GENERAL_LIST) as List<GeneralLookup>)
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
                selectedSpecialisationAdapter.submitNewItems(data?.getSerializableExtra(Constants.BundleData.GENERAL_LIST) as List<GeneralLookup>)
            }
        }


    private fun becomeInvestorResultObserver(): CustomObserverResponse<Int> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Int> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Int?
                ) {
                    viewModel.addInvestorRole()
                    InvestorMainActivity.start(requireContext())
                }
            })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as GeneralLookup
    }

}