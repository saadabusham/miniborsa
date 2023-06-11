package com.technzone.minibursa.ui.investor.invistorroles.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.common.interfaces.SeekbarCallback
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.models.country.Country
import com.technzone.minibursa.data.models.general.GeneralLookup
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.investors.CategoriesItem
import com.technzone.minibursa.databinding.FragmentBecomeInvistorBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.general.GeneralActivity
import com.technzone.minibursa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.minibursa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.minibursa.ui.investor.invistorroles.viewmodels.InvestorRoleViewModel
import com.technzone.minibursa.utils.extensions.hideKeyboard
import com.technzone.minibursa.utils.extensions.showErrorAlert
import com.technzone.minibursa.utils.extensions.validate
import com.technzone.minibursa.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint

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
            toolbarView =  binding?.layoutToolbar?.toolbar,
            tvToolbarTitleView = binding?.layoutToolbar?.tvToolbarTitle,
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
        binding?.seekBar?.setOnSeekBarChangeListener(object : SeekbarCallback {
            override fun onFromUserChange(progress: Int) {
                viewModel.investmentPrice.postValue(progress.toDouble())
            }
        })
        binding?.edBudget?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.investmentPrice.postValue(
                    binding?.edBudget?.text.toString()
                        .toDoubleOrNull() ?: viewModel.defaultMinValue.toDouble()
                )
                binding?.root.hideKeyboard(requireActivity())
                true
            } else false
        }
    }

    private fun isDataValid(): Boolean {
        binding?.edJobTitle?.text?.toString()
            ?.validate(ValidatorInputTypesEnums.NORMAL_TEXT, requireContext())?.let {
                if (!it.isValid) {
                    requireActivity().showErrorAlert(getString(R.string.job_title), it.errorMessage)
                    return false
                }
            }
        binding?.edBio?.text?.toString()
            ?.validate(ValidatorInputTypesEnums.NORMAL_TEXT, requireContext())
            ?.let {
                if (!it.isValid) {
                    requireActivity().showErrorAlert(getString(R.string.bio), it.errorMessage)
                    return false
                }
            }
        if (selectedCountriesAdapter.getSelectedItems().isEmpty()) {
            requireActivity().showErrorAlert(
                getString(R.string.app_name),
                getString(R.string.please_select_your_countries)
            )
            return false
        }
        if (selectedSpecialisationAdapter.getSelectedItems().isEmpty()) {
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
                        data?.data?.map { GeneralLookup(id = it.id, name = it.name) }
                            .let { countries ->
                                selectedCountriesAdapter.getSelectedItems()
                                    .let { selectedCountries ->
                                        countries?.forEach { count ->
                                            selectedCountries.singleOrNull { it.id == count.id }
                                                ?.let {
                                                    count.selected = true
                                                }
                                        }
                                    }
                                countries?.let { ArrayList(it) }?.let {
                                    GeneralActivity.start(
                                        requireContext(),
                                        getString(R.string.countries),
                                        it,
                                        countiesResultLauncher
                                    )
                                }
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
                                getString(R.string.choose_sectores_of_specialisations),
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
                    viewModel.setUserRole(UserRoleEnums.INVESTOR_ROLE.value)
                    InvestorMainActivity.start(requireContext())
                }
            })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as GeneralLookup
    }

}