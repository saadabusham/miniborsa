package com.technzone.miniborsa.ui.subscription.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.FragmentCompleteInvistorRoleBinding
import com.technzone.miniborsa.databinding.FragmentSubscriptionBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.invistorroles.viewmodels.InvestorRoleViewModel
import com.technzone.miniborsa.ui.subscription.viewmodel.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionFragment : BaseBindingFragment<FragmentSubscriptionBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: SubscriptionViewModel by activityViewModels()

    lateinit var selectedCountriesAdapter: SelectedGeneralRecyclerAdapter
    lateinit var selectedSpecialisationAdapter: SelectedGeneralRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_subscription

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
        binding?.btnContinue?.setOnClickListener {

        }
    }

    private fun setUpSelectedCountriesAdapter() {
        selectedCountriesAdapter = SelectedGeneralRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = selectedCountriesAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
    }
    override fun onItemClick(view: View?, position: Int, item: Any) {
//        item as GeneralLookup
    }

}