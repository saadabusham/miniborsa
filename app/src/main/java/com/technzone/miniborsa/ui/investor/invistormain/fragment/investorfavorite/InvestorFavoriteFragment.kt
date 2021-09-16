package com.technzone.miniborsa.ui.investor.invistormain.fragment.investorfavorite

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.models.investor.Business
import com.technzone.miniborsa.databinding.FragmentInvestorFavoriteBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.invistormain.fragment.investorfavorite.adapters.FavoritesAdapter
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvestorFavoriteFragment : BaseBindingFragment<FragmentInvestorFavoriteBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by activityViewModels()
    private lateinit var favoriteAdapter: FavoritesAdapter
    override fun getLayoutId(): Int = R.layout.fragment_investor_favorite

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpRvFavorites()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun setUpRvFavorites() {
        favoriteAdapter = FavoritesAdapter(requireContext())
        binding?.recyclerView?.adapter = favoriteAdapter
        binding?.recyclerView.setOnItemClickListener(this)
        loadBusinessForSale()
    }

    private fun loadBusinessForSale() {
        favoriteAdapter.submitItems(
            arrayListOf(
                Business(),
                Business(),
                Business(),
                Business(),
                Business(),
                Business(),
                Business()
            )
        )
    }
    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}