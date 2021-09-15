package com.technzone.miniborsa.ui.business.businessmain.fragments.listing.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.databinding.FragmentListingBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.businessmain.fragments.listing.adapters.ListingAdapter
import com.technzone.miniborsa.ui.business.businessmain.fragments.listing.adapters.ListingReviewAdapter
import com.technzone.miniborsa.ui.business.businessmain.fragments.listing.dialogs.SelectBusinessTypeDialog
import com.technzone.miniborsa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListingFragment : BaseBindingFragment<FragmentListingBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by activityViewModels()
    private lateinit var listingReviewAdapter: ListingReviewAdapter
    private lateinit var listingAdapter: ListingAdapter

    override fun getLayoutId(): Int = R.layout.fragment_listing

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpListingPending()
        setUpListing()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.layoutAddBusiness?.btnBusinessForSale?.setOnClickListener {
            CreateBusinessActivity.start(
                requireContext(),
                BusinessTypeEnums.BUSINESS_FOR_SALE.value
            )
        }
        binding?.layoutAddBusiness?.btnBusinessForShare?.setOnClickListener {
            CreateBusinessActivity.start(
                requireContext(),
                BusinessTypeEnums.BUSINESS_FOR_SHARE.value
            )
        }
        binding?.layoutAddBusiness?.btnBusinessFranchise?.setOnClickListener {
            CreateBusinessActivity.start(
                requireContext(),
                BusinessTypeEnums.BUSINESS_FRANCHISE.value
            )
        }
        binding?.imgAddBusiness?.setOnClickListener {
            SelectBusinessTypeDialog(requireActivity(), object : SelectBusinessTypeDialog.CallBack {
                override fun callBack(businessTypeEnums: BusinessTypeEnums) {
                    CreateBusinessActivity.start(
                        requireContext(),
                        businessTypeEnums.value
                    )
                }
            }).show()
        }
    }

    private fun setUpListingPending() {
        listingReviewAdapter = ListingReviewAdapter(requireContext())
        binding?.layoutListing?.rvPending?.adapter = listingReviewAdapter
        binding?.layoutListing?.rvPending?.setOnItemClickListener(this)
        listingReviewAdapter.submitItems(
            arrayListOf(
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
            )
        )
    }

    private fun setUpListing() {
        listingAdapter = ListingAdapter(requireContext())
        binding?.layoutListing?.rvListing?.adapter = listingAdapter
        binding?.layoutListing?.rvListing?.setOnItemClickListener(this)
        listingAdapter.submitItems(
            arrayListOf(
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
                OwnerBusiness(),
            )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }

}