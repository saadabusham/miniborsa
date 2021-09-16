package com.technzone.miniborsa.ui.core.profile.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.CommonEnums
import com.technzone.miniborsa.data.enums.MoreEnums
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.profile.More
import com.technzone.miniborsa.databinding.FragmentProfileBinding
import com.technzone.miniborsa.ui.auth.AuthActivity
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.miniborsa.ui.business.investors.activity.InvestorsActivity
import com.technzone.miniborsa.ui.core.profile.adapters.MoreRecyclerAdapter
import com.technzone.miniborsa.ui.core.profile.viewmodels.ProfileViewModel
import com.technzone.miniborsa.utils.LocaleUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: ProfileViewModel by activityViewModels()

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
        binding?.tvLogout?.setOnClickListener {
            viewModel.logout()
            AuthActivity.start(requireContext())
        }
    }

    private fun setUpAccountRecyclerView() {
        moreRecyclerAdapter = MoreRecyclerAdapter(requireContext())
        binding?.rvMore?.adapter = moreRecyclerAdapter
        binding?.rvMore?.setOnItemClickListener(this)
        initData()
    }

    private fun initData() {
        val itemList = arrayListOf<More>()
        itemList.add(
            More(
                resources.getString(R.string.more_edit_profile),
                R.drawable.ic_more_my_profile,
                MoreEnums.MY_PROFILE
            )
        )
        addItemsDependOnUserRoles(itemList)

        itemList.add(
            More(
                resources.getString(R.string.more_notification),
                R.drawable.ic_more_notifications,
                MoreEnums.NOTIFICATION
            )
        )
        itemList.add(
            More(
                resources.getString(R.string.more_recent_viewed),
                R.drawable.ic_more_recent_viewed,
                MoreEnums.RECENT_VIEWED
            )
        )
        itemList.add(
            More(
                resources.getString(R.string.more_languange),
                R.drawable.ic_more_languange,
                MoreEnums.LANGUAGE
            )
        )
        itemList.add(
            More(
                resources.getString(R.string.more_help),
                R.drawable.ic_more_help,
                MoreEnums.GET_HELP
            )
        )
        moreRecyclerAdapter.submitItems(itemList)
    }

    private fun addItemsDependOnUserRoles(itemList: ArrayList<More>) {
        when (viewModel.getCurrentUserRoles()) {
            UserRoleEnums.INVESTOR_ROLE.value -> {
                itemList.add(
                    More(
                        resources.getString(R.string.more_list_your_business),
                        R.drawable.ic_more_list_business,
                        MoreEnums.LIST_YOUR_BUSINESS
                    )
                )
            }
            UserRoleEnums.BUSINESS_ROLE.value -> {
                itemList.add(
                    More(
                        resources.getString(R.string.more_investor_list),
                        R.drawable.ic_more_list_investor,
                        MoreEnums.INVESTORS_LIST
                    )
                )
                itemList.add(
                    More(
                        resources.getString(R.string.more_switch_to_investor),
                        R.drawable.ic_more_list_business,
                        MoreEnums.SWITCH_TO_INVESTOR
                    )
                )
            }
            UserRoleEnums.VISITOR_ROLE.value -> {
                itemList.add(
                    More(
                        resources.getString(R.string.more_list_your_business),
                        R.drawable.ic_more_list_business,
                        MoreEnums.LIST_YOUR_BUSINESS
                    )
                )
                itemList.add(
                    More(
                        resources.getString(R.string.more_become_an_investor),
                        R.drawable.ic_more_become_investor,
                        MoreEnums.BECOME_INVESTOR
                    )
                )
            }
            UserRoleEnums.GUEST_ROLE.value -> {
                itemList.add(
                    More(
                        resources.getString(R.string.more_list_your_business),
                        R.drawable.ic_more_list_business,
                        MoreEnums.LIST_YOUR_BUSINESS
                    )
                )
                itemList.add(
                    More(
                        resources.getString(R.string.more_become_an_investor),
                        R.drawable.ic_more_become_investor,
                        MoreEnums.BECOME_INVESTOR
                    )
                )
            }
        }
    }

    private fun switchLanguage() {
        viewModel.saveLanguage().observe(this, {
            (this as BaseBindingActivity<*>)
                .setLanguage(
                    if (LocaleUtil.getLanguage() == CommonEnums.Languages.Arabic.value)
                        CommonEnums.Languages.Arabic.value
                    else CommonEnums.Languages.English.value
                )
        })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as More
        when (item.moreEnums) {
            MoreEnums.MY_PROFILE -> {

            }
            MoreEnums.LIST_YOUR_BUSINESS -> {
                viewModel.setCurrentUserRoles(UserRoleEnums.BUSINESS_ROLE.value)
                BusinessMainActivity.start(requireContext())
            }
            MoreEnums.INVESTORS_LIST -> {
                InvestorsActivity.start(requireContext())
            }
            MoreEnums.SWITCH_TO_INVESTOR -> {
                viewModel.setCurrentUserRoles(UserRoleEnums.INVESTOR_ROLE.value)
            }
            MoreEnums.BECOME_INVESTOR -> {

            }
            MoreEnums.NOTIFICATION -> {

            }
            MoreEnums.RECENT_VIEWED -> {

            }
            MoreEnums.LANGUAGE -> {
                switchLanguage()
            }
            MoreEnums.GET_HELP -> {

            }
        }
    }

}