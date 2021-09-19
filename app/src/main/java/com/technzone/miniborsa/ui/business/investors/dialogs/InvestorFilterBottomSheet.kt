package com.technzone.miniborsa.ui.business.investors.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.SortType
import com.technzone.miniborsa.data.models.SortModel
import com.technzone.miniborsa.data.models.investor.investors.InvestorFilter
import com.technzone.miniborsa.databinding.BottomSheetInvestorsFilterBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.business.investors.adapters.SortRecyclerAdapter
import com.technzone.miniborsa.ui.business.investors.viewmodels.InvestorsViewModel
import com.technzone.miniborsa.utils.recycleviewutils.VerticalSpaceDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorFilterBottomSheet(
    private val filter: InvestorFilter,
    private val investorsFilterCallBack: InvestorsFilterCallBack
) :
    BottomSheetDialogFragment(), BaseBindingRecyclerViewAdapter.OnItemClickListener {
    private val viewModel: InvestorsViewModel by viewModels()
    lateinit var buttomSheetCategoriesBinding: BottomSheetInvestorsFilterBinding
    lateinit var sortRecyclerAdapter: SortRecyclerAdapter
    val addressStr: MutableLiveData<String> = MutableLiveData("")
    val isShowFeatured: MutableLiveData<Boolean> = MutableLiveData(true)

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
    ): View? {
        buttomSheetCategoriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_investors_filter, null, false)
        buttomSheetCategoriesBinding.viewModel = this
        isCancelable = true
        setUpRecyclerView()
        return buttomSheetCategoriesBinding.root
    }

    private fun setUpRecyclerView() {
        sortRecyclerAdapter = SortRecyclerAdapter(requireActivity())
        buttomSheetCategoriesBinding.recyclerView.adapter = sortRecyclerAdapter
        buttomSheetCategoriesBinding.recyclerView.setOnItemClickListener(this)
        buttomSheetCategoriesBinding.recyclerView.addItemDecoration(
            VerticalSpaceDecoration(
                resources.getDimension(R.dimen._5sdp).toInt(),
                0
            )
        )
        sortRecyclerAdapter.submitItems(
            arrayListOf(
                SortModel(
                    activity?.getString(R.string.sort_a_z),
                    type = SortType.A_Z.type,
                    selected = MutableLiveData(true)
                ),
                SortModel(
                    activity?.getString(R.string.sort_by_default),
                    type = SortType.DEFAULT.type
                ),
                SortModel(
                    activity?.getString(R.string.sort_by_investments),
                    type = SortType.INVESTMENTS.type
                ),
            )
        )

    }

    fun onDoneClicked() {
        filter.apply {

        }
        investorsFilterCallBack.callBack(filter)
        dismiss()
    }

    fun onCancelClicked() {
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    interface InvestorsFilterCallBack {
        fun callBack(investorFilter: InvestorFilter)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
//        item as
//        dismiss()
//        cityPickerCallBack.callBack(city = item)
    }
}