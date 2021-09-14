package com.technzone.miniborsa.ui.business.createbusiness.fragments

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.models.map.Address
import com.technzone.miniborsa.databinding.FragmentCreateBusinessStep1Binding
import com.technzone.miniborsa.ui.base.dialogs.DialogsUtil
import com.technzone.miniborsa.ui.base.fragment.BaseFormBindingFragment
import com.technzone.miniborsa.ui.business.createbusiness.viewmodels.CreateBusinessViewModel
import com.technzone.miniborsa.ui.map.MapActivity
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
    }
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                viewModel.address.value = data?.getSerializableExtra(Constants.BundleData.ADDRESS) as Address
                binding?.tvLocation?.text =
                    getLocationName(
                        viewModel.address.value?.lat,
                        viewModel.address.value?.lon
                    ).also {
                        viewModel.addressString.postValue(it)
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
        callback(true)
    }

}