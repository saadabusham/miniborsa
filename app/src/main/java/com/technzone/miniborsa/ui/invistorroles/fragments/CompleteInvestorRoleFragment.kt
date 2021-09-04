package com.technzone.miniborsa.ui.invistorroles.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.general.GeneralLookup
import com.technzone.miniborsa.databinding.FragmentCompleteInvistorRoleBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.invistorroles.viewmodels.InvestorRoleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompleteInvestorRoleFragment : BaseBindingFragment<FragmentCompleteInvistorRoleBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorRoleViewModel by activityViewModels()

    lateinit var selectedCountriesAdapter: SelectedGeneralRecyclerAdapter
    lateinit var selectedSpecialisationAdapter: SelectedGeneralRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_complete_invistor_role

    override fun onViewVisible() {
        super.onViewVisible()
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
    }

    private fun setUpSelectedCountriesAdapter() {
        selectedCountriesAdapter = SelectedGeneralRecyclerAdapter(requireContext())
        binding?.rvSelectedCountries?.adapter = selectedCountriesAdapter
        binding?.rvSelectedCountries?.setOnItemClickListener(this)
    }

    private fun setUpSelectedSpecialistAdapter() {
        selectedSpecialisationAdapter = SelectedGeneralRecyclerAdapter(requireContext())
        binding?.rvSelectedSpecialisation?.adapter = selectedSpecialisationAdapter
        binding?.rvSelectedSpecialisation?.setOnItemClickListener(this)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as GeneralLookup
    }

}