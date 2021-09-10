package com.technzone.miniborsa.ui.profile.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.MoreEnums
import com.technzone.miniborsa.data.models.profile.More
import com.technzone.miniborsa.databinding.FragmentProfileBinding
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.investor.invistormain.viewmodels.InvestorMainViewModel
import com.technzone.miniborsa.ui.profile.adapters.MoreRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: InvestorMainViewModel by activityViewModels()

    private lateinit var moreRecyclerAdapter: MoreRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpAccountRecyclerView()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun setUpAccountRecyclerView() {
        moreRecyclerAdapter = MoreRecyclerAdapter(requireContext())
        binding?.rvMore?.adapter = moreRecyclerAdapter
        binding?.rvMore?.setOnItemClickListener(this)
        initData()
    }

    private fun initData() {
        val itemList = arrayListOf<More>()

        itemList.addAll(
            arrayListOf(
                More(
                    resources.getString(R.string.more_edit_profile),
                    R.drawable.ic_more_my_profile,
                    MoreEnums.MY_PROFILE
                ),
                More(
                    resources.getString(R.string.more_list_your_business),
                    R.drawable.ic_more_list_business,
                    MoreEnums.LIST_YOUR_BUSINESS
                ),
                More(
                    resources.getString(R.string.more_become_an_investor),
                    R.drawable.ic_more_become_investor,
                    MoreEnums.BECOME_INVESTOR
                ),
                More(
                    resources.getString(R.string.more_notification),
                    R.drawable.ic_more_notifications,
                    MoreEnums.NOTIFICATION
                ),
                More(
                    resources.getString(R.string.more_recent_viewed),
                    R.drawable.ic_more_recent_viewed,
                    MoreEnums.RECENT_VIEWED
                ),
                More(
                    resources.getString(R.string.more_languange),
                    R.drawable.ic_more_languange,
                    MoreEnums.LANGUAGE
                ),
                More(
                    resources.getString(R.string.more_help),
                    R.drawable.ic_more_help,
                    MoreEnums.GET_HELP
                )
            )
        )
        moreRecyclerAdapter.submitItems(itemList)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as More
        when (item.moreEnums) {
            MoreEnums.MY_PROFILE -> {

            }
            MoreEnums.LIST_YOUR_BUSINESS -> {

            }
            MoreEnums.BECOME_INVESTOR -> {

            }
            MoreEnums.NOTIFICATION -> {

            }
            MoreEnums.RECENT_VIEWED -> {

            }
            MoreEnums.LANGUAGE -> {

            }
            MoreEnums.GET_HELP -> {

            }
        }
    }

}