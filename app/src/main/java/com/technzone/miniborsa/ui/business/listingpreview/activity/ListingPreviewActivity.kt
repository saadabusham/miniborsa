package com.technzone.miniborsa.ui.business.listingpreview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.models.business.ListingItem
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.investor.ExtraInfo
import com.technzone.miniborsa.databinding.ActivityListingPreviewBinding
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.miniborsa.ui.business.listingpreview.adapters.ListingItemAdapter
import com.technzone.miniborsa.ui.business.listingpreview.dialog.SubmittedDialogFragment
import com.technzone.miniborsa.ui.business.listingpreview.viewmodel.ListingPreviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class ListingPreviewActivity : BaseBindingActivity<ActivityListingPreviewBinding>() {


    private val viewModel: ListingPreviewViewModel by viewModels()

    private lateinit var listingItemAdapter: ListingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_listing_preview,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.list_business
        )
        setUpBinding()
        setUpListeners()
        setUpRvExtraInfo()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnSubmit?.setOnClickListener {
            showSubmittedDialog()
        }
    }

    private fun showSubmittedDialog() {
        val dialog = SubmittedDialogFragment(this)
        dialog.setOnDismissListener {
            BusinessMainActivity.start(this)
        }
        dialog.show()
    }

    private fun setUpRvExtraInfo() {
        listingItemAdapter = ListingItemAdapter(this)
        binding?.rvListingItems?.adapter = listingItemAdapter
        binding?.rvListingItems?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                item as ExtraInfo
//                if (item.selected.value == true)
//                    viewModel.selectedItemsCount.value = viewModel.selectedItemsCount.value?.plus(1)
//                else
//                    viewModel.selectedItemsCount.value =
//                        viewModel.selectedItemsCount.value?.minus(1)
            }
        })
        listingItemAdapter.submitItems(
            arrayListOf(
                ListingItem(
                    title = "Title",
                    description = "Lorem ipsum dolor sit amet.",
                    percent = 78
                ),
                ListingItem(
                    title = "Location Info",
                    description = "Lorem ipsum dolor sit amet.",
                    percent = 80
                ),
                ListingItem(
                    title = "Media",
                    description = "Lorem ipsum dolor sit amet.",
                    percent = 95
                ),
                ListingItem(
                    title = "More Details",
                    description = "Lorem ipsum dolor sit amet.",
                    percent = 88
                )
            )
        )
    }

    companion object {
        fun start(
            context: Context?,
            ownerBusiness:OwnerBusiness
        ) {
            val intent = Intent(context, ListingPreviewActivity::class.java).apply {
                putExtra(Constants.BundleData.OWNER_BUSINESS,ownerBusiness)
            }
            context?.startActivity(intent)
        }
    }
}