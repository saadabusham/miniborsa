package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.data.models.general.ListWrapper
import com.technzone.miniborsa.data.models.investor.investors.CategoriesItem
import com.technzone.miniborsa.data.models.map.Address
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep1Binding
import com.technzone.miniborsa.ui.base.dialogs.DialogsUtil
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.base.sheet.lookupselector.LookupSelectorBottomSheet
import com.technzone.miniborsa.ui.business.createbusiness.dialogs.CreateCompanyDialog
import com.technzone.miniborsa.ui.map.MapActivity
import com.technzone.miniborsa.utils.extensions.calculatePercentage
import com.technzone.miniborsa.utils.getLocationName
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateBusinessStep1Fragment : BaseFormBindingFragment<FragmentCreateBusinessStep1Binding>() {

    override fun getLayoutId(): Int = R.layout.fragment_create_business_step1

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        handleBuniness()
        observeInputs()
        calculatePercentage()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.tvEstablishYear?.setOnClickListener {
            onDateClicked()
        }
        binding?.btnPinLocation?.setOnClickListener {
            MapActivity.start(requireActivity(), resultLauncher)
        }
        binding?.edAddress?.setOnClickListener {
            MapActivity.start(requireActivity(), resultLauncher)
        }
        binding?.tvCat1?.setOnClickListener {
            viewModel.getCategories()
                .observe(this, requestCompanyResultObserver(viewModel.getCategoriesCount(1)))
        }
        binding?.tvCat2?.setOnClickListener {
            viewModel.getCategories()
                .observe(this, requestCompanyResultObserver(viewModel.getCategoriesCount(2)))
        }
        binding?.tvCat3?.setOnClickListener {
            viewModel.getCategories()
                .observe(this, requestCompanyResultObserver(viewModel.getCategoriesCount(3)))
        }
    }
    private fun handleBuniness() {
        if (viewModel.hasBusiness && !viewModel.businessDraft) {
            viewModel.requestBusiness().observe(this, requestBusinessResultObserver())
        } else if (viewModel.hasBusiness || viewModel.companyDraft || viewModel.businessDraft) {
            viewModel.getCategories().observe(this, requestCompanyResultObserver(0, true))
        } else {
            showCreateDialog(false)
        }
    }

    private fun showCreateDialog(request: Boolean) {
        CreateCompanyDialog(requireActivity(), object : CreateCompanyDialog.CompanyNameCallBack {
            override fun save(name: String, dialog: CreateCompanyDialog) {
                if (!request)
                    viewModel.requestCompany(name)
                        .observe(
                            this@CreateBusinessStep1Fragment,
                            requestCompanyResultObserver(dialog = dialog)
                        )
                else viewModel.requestBusiness()
                    .observe(
                        this@CreateBusinessStep1Fragment,
                        requestCompanyResultObserver(dialog = dialog)
                    )
            }

            override fun cancel() {
                requireActivity().finish()
            }
        }).show()
    }

    private fun requestCompanyResultObserver(
        dialog: CreateCompanyDialog
    ): CustomObserverResponse<Int> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Int> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Int?
                ) {
                    viewModel.businessId = data
                    dialog.dismiss()
                }

            })
    }

    private fun requestBusinessResultObserver(
    ): CustomObserverResponse<Int> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<Int> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Int?
                ) {
                    viewModel.businessId = data
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    requireActivity().finish()
                }
            })
    }

    private fun requestCompanyResultObserver(
        categoryNumber: Int,
        fillOldData: Boolean = false
    ): CustomObserverResponse<ListWrapper<CategoriesItem>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<ListWrapper<CategoriesItem>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ListWrapper<CategoriesItem>?
                ) {
                    if (!data?.data.isNullOrEmpty())
                        if (fillOldData) {
                            viewModel.categories.withIndex().forEach { cat ->
                                data?.data?.singleOrNull { it.id == cat.value }?.let {
                                    updateCategories(
                                        cat.index + 1,
                                        GeneralLookup(id = it.id, name = it.name)
                                    )
                                }
                            }
                        } else {
                            data?.data?.filter { !viewModel.categories.contains(it.id) }?.let {
                                it.map { GeneralLookup(id = it.id, name = it.name) }?.let {
                                    showCategorySheet(categoryNumber, it)
                                }
                            }
                        }
                }
            })
    }

    private fun showCategorySheet(categoryNumber: Int, list: List<GeneralLookup>) {
        LookupSelectorBottomSheet(callBack = object :
            LookupSelectorBottomSheet.LookupSelectorCallBack {
            override fun callBack(selectedItem: GeneralLookup) {
                updateCategories(categoryNumber, selectedItem)
            }
        }, list = list, fullScreen = true).show(childFragmentManager, "Lookup")
    }

    private fun updateCategories(categoryNumber: Int, selectedItem: GeneralLookup) {
        when (categoryNumber) {
            1 -> {
                binding?.tvCat1?.text = selectedItem.name
                selectedItem.id?.let {
                    if (viewModel.categories.size >= 1) viewModel.categories.set(
                        0,
                        it
                    ) else viewModel.categories.add(0, it)
                }
            }
            2 -> {
                binding?.tvCat2?.text = selectedItem.name
                selectedItem.id?.let {
                    if (viewModel.categories.size >= 2) viewModel.categories.set(
                        1,
                        it
                    ) else viewModel.categories.add(1, it)
                }
            }
            else -> {
                binding?.tvCat3?.text = selectedItem.name
                selectedItem.id?.let {
                    if (viewModel.categories.size >= 3) viewModel.categories.set(
                        2,
                        it
                    ) else viewModel.categories.add(2, it)
                }
            }
        }
        calculatePercentage()
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                viewModel.address.value =
                    data?.getSerializableExtra(Constants.BundleData.ADDRESS) as Address
                getLocationName(
                    viewModel.address.value?.lat,
                    viewModel.address.value?.lon
                ).also {
                    viewModel.addressStr.postValue(it)
                }
            }
        }

    private fun onDateClicked() {
        requireActivity().let {
            DialogsUtil.showDatePickerDialog(
                context = it,
                calBefore = Calendar.getInstance(),
                minDate = null,
                showDays = false,
                showMonths = false,
                showYears = true,
                maxDate = Calendar.getInstance().timeInMillis,
                listener = { _, year, month, dayOfMonth ->
                    handleUserSelectIssueDate(
                        year
                    )
                })
        }
    }

    private fun handleUserSelectIssueDate(year: Int) {
        binding?.tvEstablishYear?.text =
            year.toString().also {
                viewModel.date.value = it
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


    private fun observeInputs() {
        viewModel.title.observe(this,{
            calculatePercentage()
        })
        viewModel.summery.observe(this,{
            calculatePercentage()
        })
        viewModel.addressStr.observe(this,{
            calculatePercentage()
        })
        viewModel.date.observe(this,{
            calculatePercentage()
        })
    }

    override fun calculatePercentage() {
        viewModel.percentage.postValue(viewModel.buildBusinessRequest().calculatePercentage())
    }

}