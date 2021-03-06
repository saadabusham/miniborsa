package com.technzone.minibursa.ui.business.investors.dialogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.SortType
import com.technzone.minibursa.data.models.SortModel
import com.technzone.minibursa.data.models.country.Country
import com.technzone.minibursa.data.models.general.GeneralLookup
import com.technzone.minibursa.data.models.general.ListWrapper
import com.technzone.minibursa.data.models.investor.investors.InvestorFilter
import com.technzone.minibursa.databinding.BottomSheetInvestorsFilterBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.business.investors.adapters.SortRecyclerAdapter
import com.technzone.minibursa.ui.business.investors.viewmodels.InvestorsViewModel
import com.technzone.minibursa.ui.general.GeneralActivity
import com.technzone.minibursa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.minibursa.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorFilterBottomSheet(
    private val filter: InvestorFilter,
    private val investorsFilterCallBack: InvestorsFilterCallBack
) :
    BottomSheetDialogFragment(), BaseBindingRecyclerViewAdapter.OnItemClickListener {
    private val viewModel: InvestorsViewModel by viewModels()
    lateinit var binding: BottomSheetInvestorsFilterBinding
    lateinit var sortRecyclerAdapter: SortRecyclerAdapter
    lateinit var selectedCountriesAdapter: SelectedGeneralRecyclerAdapter
    val isShowFeatured: MutableLiveData<Boolean> = MutableLiveData(filter.isFeatured)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onStart() {
        super.onStart()
        view?.post {
            val parent = requireView().parent as View
            val params = (parent).layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior
            bottomSheetBehavior.peekHeight = requireView().measuredHeight
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_investors_filter, null, false)
        binding.viewModel = this
        isCancelable = true
        setUpRecyclerView()
        setUpSelectedCountriesAdapter()
        return binding.root
    }

    private fun setUpRecyclerView() {
        sortRecyclerAdapter = SortRecyclerAdapter(requireActivity())
        binding.recyclerView.adapter = sortRecyclerAdapter
        binding.recyclerView.setOnItemClickListener(this)
        binding.recyclerView.addItemDecoration(
            VerticalSpaceDecoration(
                resources.getDimension(R.dimen._5sdp).toInt(),
                0
            )
        )
        sortRecyclerAdapter.submitItems(
            arrayListOf(
                SortModel(
                    activity?.getString(R.string.sort_a_z),
                    type = SortType.A_Z.type
                ),
                SortModel(
                    activity?.getString(R.string.sort_by_default),
                    type = SortType.DEFAULT.type
                ),
                SortModel(
                    activity?.getString(R.string.sort_by_investments),
                    type = SortType.INVESTMENTS.type
                )
            )
        )
        sortRecyclerAdapter.items.withIndex().singleOrNull { it.value.type == filter.sortType }?.let {
            it.value.selected.postValue(true)
            sortRecyclerAdapter.notifyItemChanged(it.index)
        } ?: also {
            sortRecyclerAdapter.items[0].selected.postValue(true)
            sortRecyclerAdapter.notifyItemChanged(0)
        }
    }

    fun onDoneClicked() {
        filter.apply {
            isFeatured = isShowFeatured.value == true
            sortType = sortRecyclerAdapter.getSelectedItem()?.type
            countries = selectedCountriesAdapter.getSelectedItems().map { it.id ?: 0 }
            selectedCountries = selectedCountriesAdapter.getSelectedItems()
        }
        investorsFilterCallBack.callBack(filter)
        dismiss()
    }

    fun onCancelClicked() {
        dismiss()
    }

    fun onCountriesClicked() {
        viewModel.getCountries().observe(this, countriesResultObserver())
    }

    private fun setUpSelectedCountriesAdapter() {
        selectedCountriesAdapter = SelectedGeneralRecyclerAdapter(requireContext(), true)
        binding.rvSelectedCountries.adapter = selectedCountriesAdapter
        binding.rvSelectedCountries.setOnItemClickListener(this)
        filter.selectedCountries?.let {
            selectedCountriesAdapter.submitNewItems(it)
        }
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
                            selectedCountriesAdapter.items.forEach { new ->
                                it?.singleOrNull { it.id == new.id }?.let {
                                    it.selected = true
                                }
                            }
                            selectedCountriesAdapter.items.filter { it.selected }
                            GeneralActivity.start(
                                requireContext(),
                                getString(R.string.countries),
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


    interface InvestorsFilterCallBack {
        fun callBack(investorFilter: InvestorFilter)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {
            is GeneralLookup -> {
                selectedCountriesAdapter.items.removeAt(position)
                selectedCountriesAdapter.notifyItemRemoved(position)
            }
        }
    }
}