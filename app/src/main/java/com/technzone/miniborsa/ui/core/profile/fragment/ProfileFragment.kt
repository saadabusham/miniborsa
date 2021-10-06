package com.technzone.miniborsa.ui.core.profile.fragment

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.technzone.miniborsa.R
import com.technzone.miniborsa.common.CommonEnums
import com.technzone.miniborsa.common.interfaces.LoginCallBack
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.common.CustomObserverResponse
import com.technzone.miniborsa.data.enums.MoreEnums
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.miniborsa.data.models.business.business.OwnerBusiness
import com.technzone.miniborsa.data.models.profile.More
import com.technzone.miniborsa.databinding.FragmentProfileBinding
import com.technzone.miniborsa.ui.auth.AuthActivity
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.miniborsa.ui.base.dialogs.DialogsUtil.showLoginDialog
import com.technzone.miniborsa.ui.base.dialogs.LoginDialog
import com.technzone.miniborsa.ui.base.fragment.BaseBindingFragment
import com.technzone.miniborsa.ui.business.businessdraft.activity.BusinessDraftActivity
import com.technzone.miniborsa.ui.business.businessdraft.viewmodels.BusinessDraftViewModel
import com.technzone.miniborsa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.miniborsa.ui.business.investors.activity.InvestorsActivity
import com.technzone.miniborsa.ui.core.faqs.FaqsActivity
import com.technzone.miniborsa.ui.core.profile.adapters.MoreRecyclerAdapter
import com.technzone.miniborsa.ui.core.profile.viewmodels.ProfileViewModel
import com.technzone.miniborsa.ui.core.settings.SettingsActivity
import com.technzone.miniborsa.ui.core.updateprofile.UpdateProfileActivity
import com.technzone.miniborsa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.miniborsa.ui.investor.invistorroles.activity.InvestorRolesActivity
import com.technzone.miniborsa.ui.investor.recentviewed.activity.RecentViewActivity
import com.technzone.miniborsa.ui.subscription.activity.SubscriptionActivity
import com.technzone.miniborsa.utils.LocaleUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseBindingFragment<FragmentProfileBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener, LoginCallBack {

    private val viewModel: ProfileViewModel by activityViewModels()
    private val businessDraftViewModel: BusinessDraftViewModel by activityViewModels()

    private lateinit var moreRecyclerAdapter: MoreRecyclerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_profile

    override fun onResume() {
        super.onResume()
        viewModel.getMyProfile().observe(this, profileObserver())
    }

    override fun onViewVisible() {
        super.onViewVisible()
        init()
    }

    private fun init() {
        if (requireActivity() is InvestorMainActivity) (requireActivity() as InvestorMainActivity).loginCallBack =
            this
//        else (requireActivity() as BusinessMainActivity).loginCallBack = this
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

    private fun profileObserver(): CustomObserverResponse<UserDetailsResponseModel> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<UserDetailsResponseModel> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: UserDetailsResponseModel?
                ) {
                    data?.let { it1 -> viewModel.storeUser(it1) }
                    viewModel.getUser()
                }
            }, showError = false
        )
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
                resources.getString(R.string.more_settings),
                R.drawable.ic_more_setting,
                MoreEnums.SETTINGS
            )
        )
//        itemList.add(
//            More(
//                resources.getString(R.string.more_languange),
//                R.drawable.ic_more_languange,
//                MoreEnums.LANGUAGE
//            )
//        )
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
                        resources.getString(R.string.more_switch_to_business_owner),
                        R.drawable.ic_more_list_business,
                        MoreEnums.SWITCH_TO_BUSINESS
                    )
                )
//                itemList.add(
//                    More(
//                        resources.getString(R.string.more_notification),
//                        R.drawable.ic_more_notifications,
//                        MoreEnums.NOTIFICATION
//                    )
//                )
                itemList.add(
                    More(
                        resources.getString(R.string.more_recent_viewed),
                        R.drawable.ic_more_recent_viewed,
                        MoreEnums.RECENT_VIEWED
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
                        resources.getString(R.string.more_switch_to_business_owner),
                        R.drawable.ic_more_list_business,
                        MoreEnums.SWITCH_TO_BUSINESS
                    )
                )
                itemList.add(
                    More(
                        resources.getString(R.string.more_switch_to_investor),
                        R.drawable.ic_more_become_investor,
                        MoreEnums.SWITCH_TO_INVESTOR
                    )
                )
//                itemList.add(
//                    More(
//                        resources.getString(R.string.more_notification),
//                        R.drawable.ic_more_notifications,
//                        MoreEnums.NOTIFICATION
//                    )
//                )
                itemList.add(
                    More(
                        resources.getString(R.string.more_recent_viewed),
                        R.drawable.ic_more_recent_viewed,
                        MoreEnums.RECENT_VIEWED
                    )
                )
            }
            UserRoleEnums.GUEST_ROLE.value -> {
                itemList.add(
                    More(
                        resources.getString(R.string.more_switch_to_business_owner),
                        R.drawable.ic_more_list_business,
                        MoreEnums.SWITCH_TO_BUSINESS
                    )
                )
                itemList.add(
                    More(
                        resources.getString(R.string.more_switch_to_investor),
                        R.drawable.ic_more_become_investor,
                        MoreEnums.SWITCH_TO_INVESTOR
                    )
                )
//                itemList.add(
//                    More(
//                        resources.getString(R.string.more_notification),
//                        R.drawable.ic_more_notifications,
//                        MoreEnums.NOTIFICATION
//                    )
//                )
            }
        }
    }

    private fun switchLanguage() {
        viewModel.saveLanguage().observe(this, {
            (requireActivity() as BaseBindingActivity<*>)
                .setLanguage(
                    if (LocaleUtil.getLanguage() == CommonEnums.Languages.English.value)
                        CommonEnums.Languages.Arabic.value
                    else CommonEnums.Languages.English.value
                )
        })
    }

    private fun showLoginDialog() {
        requireActivity().showLoginDialog(object : LoginDialog.CallBack {
            override fun callBackLogin() {
                AuthActivity.startForResult(requireActivity(), true)
            }
        })
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as More
        when (item.moreEnums) {
            MoreEnums.MY_PROFILE -> {
                if (viewModel.isUserLoggedIn()) {
                    UpdateProfileActivity.start(requireContext())
                } else {
                    showLoginDialog()
                }
            }
            MoreEnums.SWITCH_TO_BUSINESS -> {
                if (viewModel.isUserLoggedIn()) {
                    if (viewModel.isUserHasBusinessRoles()) {
                        viewModel.setCurrentUserRoles(UserRoleEnums.BUSINESS_ROLE.value)
                        BusinessMainActivity.start(requireContext())
                    } else {
                        businessDraftViewModel.getPendingListing()
                            .observe(this, pendingResultObserver())
                    }
                } else {
                    showLoginDialog()
                }
            }
            MoreEnums.INVESTORS_LIST -> {
                InvestorsActivity.start(requireContext())
            }
            MoreEnums.SWITCH_TO_INVESTOR -> {
                if (viewModel.isUserLoggedIn()) {
                    if (viewModel.isUserHasInvestorRoles()) {
                        viewModel.setCurrentUserRoles(UserRoleEnums.INVESTOR_ROLE.value)
                        InvestorMainActivity.start(requireContext())
                    } else {
                        InvestorRolesActivity.start(requireContext())
                    }
                } else {
                    showLoginDialog()
                }
            }
            MoreEnums.NOTIFICATION -> {
                if (viewModel.isUserLoggedIn()) {
                    navigationController.navigate(
                        R.id.action_nav_profile_to_notificationFragment,
                        bundleOf(Pair(Constants.BundleData.SHOW_BACK, true))
                    )
                } else {
                    showLoginDialog()
                }
            }

            MoreEnums.RECENT_VIEWED -> {
                if (viewModel.isUserLoggedIn()) {
                    RecentViewActivity.start(requireContext())
                } else {
                    showLoginDialog()
                }
            }

            MoreEnums.LANGUAGE -> {
                switchLanguage()
            }
            MoreEnums.SETTINGS -> {
                SettingsActivity.start(requireContext())
            }
            MoreEnums.GET_HELP -> {
                FaqsActivity.start(requireContext())
            }
        }
    }

    private fun pendingResultObserver(): CustomObserverResponse<OwnerBusiness> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<OwnerBusiness> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: OwnerBusiness?
                ) {
                    data?.let {
                        BusinessDraftActivity.start(requireContext())
                    } ?: also {
                        SubscriptionActivity.start(
                            requireContext(),
                            isBusiness = true,
                            hasBusiness = false,
                            clearTask = false
                        )
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    SubscriptionActivity.start(
                        requireContext(),
                        isBusiness = true,
                        hasBusiness = false,
                        clearTask = false
                    )
                }
            }, showError = false
        )
    }

    override fun loggedInSuccess() {
        moreRecyclerAdapter.clear()
        initData()
    }
}