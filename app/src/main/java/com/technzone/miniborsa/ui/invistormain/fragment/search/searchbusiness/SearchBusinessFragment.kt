package com.technzone.miniborsa.ui.invistormain.fragment.search.searchbusiness

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.FragmentSearchBusinessBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.invistormain.fragment.search.filter.FilterActivity
import com.technzone.miniborsa.ui.invistormain.viewmodels.InvestorMainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchBusinessFragment : BaseBindingFragment<FragmentSearchBusinessBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by activityViewModels()


    override fun getLayoutId(): Int {
        return R.layout.fragment_search_business
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition: Transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.imgBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding?.tvFilter?.setOnClickListener {
            FilterActivity.start(requireContext())
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }


}