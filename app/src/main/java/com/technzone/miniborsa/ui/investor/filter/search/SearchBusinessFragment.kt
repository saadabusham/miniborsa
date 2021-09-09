package com.technzone.miniborsa.ui.investor.filter.search

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.activityViewModels
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.technzone.miniborsa.R
import com.technzone.miniborsa.databinding.FragmentSearchBusinessBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.filter.viewmodels.FilterBusinessViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchBusinessFragment : BaseBindingFragment<FragmentSearchBusinessBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: FilterBusinessViewModel by activityViewModels()


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
        binding?.cvSearch?.viewTreeObserver?.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                binding?.cvSearch?.viewTreeObserver?.removeOnPreDrawListener ( this )
                requireActivity().startPostponedEnterTransition()
                return true
            }
        })
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
            navigationController.navigate(R.id.action_searchBusinessFragment_to_filterBusinessFragment)
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }


}