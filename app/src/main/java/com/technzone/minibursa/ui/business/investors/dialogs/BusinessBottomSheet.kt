package com.technzone.minibursa.ui.business.investors.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.technzone.minibursa.R
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.databinding.BottomSheetBusinessBinding
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.business.businesschannels.adapters.BusinessChannelsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessBottomSheet(
    private val businessList: List<OwnerBusiness>,
    private val businessCallBack: BusinessCallBack
) : BottomSheetDialogFragment(), BaseBindingRecyclerViewAdapter.OnItemClickListener {
    lateinit var binding: BottomSheetBusinessBinding
    lateinit var businessChannelsAdapter: BusinessChannelsAdapter

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
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_business, null, false)
        binding.viewModel = this
        isCancelable = true
        setUpSelectedCountriesAdapter()
        return binding.root
    }

    private fun setUpSelectedCountriesAdapter() {
        businessChannelsAdapter = BusinessChannelsAdapter(requireContext())
        binding.recyclerView.adapter = businessChannelsAdapter
        binding.recyclerView.setOnItemClickListener(this)
        businessChannelsAdapter.submitItems(businessList)
    }

    interface BusinessCallBack {
        fun callBack(business: OwnerBusiness)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as OwnerBusiness
        businessCallBack.callBack(item)
        dismiss()
    }
}