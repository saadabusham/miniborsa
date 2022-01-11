package com.technzone.minibursa.ui.core.profile.fragment

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import com.technzone.minibursa.R
import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.common.interfaces.LoginCallBack
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.BusinessTypeEnums
import com.technzone.minibursa.data.enums.MoreEnums
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.data.models.profile.More
import com.technzone.minibursa.databinding.FragmentProfileBinding
import com.technzone.minibursa.ui.auth.AuthActivity
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.minibursa.ui.base.dialogs.DialogsUtil.showLoginDialog
import com.technzone.minibursa.ui.base.dialogs.LoginDialog
import com.technzone.minibursa.ui.base.fragment.BaseBindingFragment
import com.technzone.minibursa.ui.business.businessdraft.activity.BusinessDraftActivity
import com.technzone.minibursa.ui.business.businessdraft.viewmodels.BusinessDraftViewModel
import com.technzone.minibursa.ui.business.businessmain.activity.BusinessMainActivity
import com.technzone.minibursa.ui.business.businessmain.fragments.listing.dialogs.SelectBusinessTypeDialog
import com.technzone.minibursa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.minibursa.ui.business.investors.activity.InvestorsActivity
import com.technzone.minibursa.ui.core.faqs.FaqsActivity
import com.technzone.minibursa.ui.core.profile.adapters.MoreRecyclerAdapter
import com.technzone.minibursa.ui.core.profile.viewmodels.ProfileViewModel
import com.technzone.minibursa.ui.core.settings.SettingsActivity
import com.technzone.minibursa.ui.core.updateprofile.UpdateProfileActivity
import com.technzone.minibursa.ui.dataview.dataviewer.DataViewerActivity
import com.technzone.minibursa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.minibursa.ui.investor.invistorroles.activity.InvestorRolesActivity
import com.technzone.minibursa.ui.investor.recentviewed.activity.RecentViewActivity
import com.technzone.minibursa.utils.LocaleUtil
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
        itemList.add(
            More(
                resources.getString(R.string.more_terms_and_conditions),
                R.drawable.ic_more_terms_conditions,
                MoreEnums.TERMS_AND_CONDITIONS
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
            MoreEnums.TERMS_AND_CONDITIONS -> {
                DataViewerActivity.start(
                    requireContext(),
                    resources.getString(R.string.more_terms_and_conditions),
                    viewModel.getTermsAndConditions()
                )
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
                        BusinessDraftActivity.start(requireActivity())
                    } ?: also {
                        showSelectTypeDialog()
                    }
                }

                override fun onError(subErrorCode: ResponseSubErrorsCodeEnum, message: String) {
                    super.onError(subErrorCode, message)
                    showSelectTypeDialog()
                }
            }, showError = false
        )
    }

    private fun showSelectTypeDialog() {
        SelectBusinessTypeDialog(requireActivity(), object : SelectBusinessTypeDialog.CallBack {
            override fun callBack(businessTypeEnums: BusinessTypeEnums) {
                CreateBusinessActivity.start(
                    context = requireActivity(),
                    businessType = businessTypeEnums.value,
                    hasBusiness = false,
                    companyDraft = false
                )
            }
        }).show()
    }

    override fun loggedInSuccess() {
        moreRecyclerAdapter.clear()
        initData()
    }
}