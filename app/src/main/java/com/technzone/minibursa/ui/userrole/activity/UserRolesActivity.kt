package com.technzone.minibursa.ui.userrole.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.technzone.minibursa.R
import com.technzone.minibursa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.minibursa.data.common.CustomObserverResponse
import com.technzone.minibursa.data.enums.BusinessTypeEnums
import com.technzone.minibursa.data.enums.UserRoleEnums
import com.technzone.minibursa.data.models.auth.login.UserRoles
import com.technzone.minibursa.data.models.business.business.OwnerBusiness
import com.technzone.minibursa.databinding.ActivityUserRoleBinding
import com.technzone.minibursa.ui.base.activity.BaseBindingActivity
import com.technzone.minibursa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.minibursa.ui.business.businessdraft.activity.BusinessDraftActivity
import com.technzone.minibursa.ui.business.businessmain.fragments.listing.dialogs.SelectBusinessTypeDialog
import com.technzone.minibursa.ui.business.createbusiness.activity.CreateBusinessActivity
import com.technzone.minibursa.ui.investor.invistormain.activity.InvestorMainActivity
import com.technzone.minibursa.ui.investor.invistorroles.activity.InvestorRolesActivity
import com.technzone.minibursa.ui.userrole.adapters.UserRolesRecyclerAdapter
import com.technzone.minibursa.ui.userrole.viewmodel.UserRolesViewModel
import com.technzone.minibursa.utils.extensions.showErrorAlert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRolesActivity : BaseBindingActivity<ActivityUserRoleBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: UserRolesViewModel by viewModels()
    lateinit var adapter: UserRolesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            layoutResID = R.layout.activity_user_role,
            hasToolbar = false
        )
        init()
    }

    private fun init() {
        setUpListeners()
        setUpAdapter()
    }

    private fun setUpListeners() {
        binding?.btnContinue?.setOnClickListener {
            adapter.getSelectedItem()?.let {
                when (it.role) {
                    UserRoleEnums.BUSINESS_ROLE.value -> {
                        viewModel.getPendingListing()
                            .observe(this@UserRolesActivity, pendingResultObserver())
                    }
                    UserRoleEnums.INVESTOR_ROLE.value -> {
                        InvestorRolesActivity.start(this)
                    }
                    UserRoleEnums.VISITOR_ROLE.value -> {
                        viewModel.setUserRole(UserRoleEnums.VISITOR_ROLE.value)
                        InvestorMainActivity.start(this)
                    }
                }
            } ?: also {
                showErrorAlert(
                    getString(R.string.user_role),
                    getString(R.string.please_select_your_role)
                )
            }
        }
    }

    private fun pendingResultObserver(): CustomObserverResponse<OwnerBusiness> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<OwnerBusiness> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: OwnerBusiness?
                ) {
                    data?.let {
                        viewModel.setUserRole(UserRoleEnums.VISITOR_ROLE.value)
                        BusinessDraftActivity.start(this@UserRolesActivity, forResult = true)
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
        SelectBusinessTypeDialog(this, object : SelectBusinessTypeDialog.CallBack {
            override fun callBack(businessTypeEnums: BusinessTypeEnums) {
                viewModel.setUserRole(UserRoleEnums.VISITOR_ROLE.value)
                CreateBusinessActivity.start(
                    context = this@UserRolesActivity,
                    businessType = businessTypeEnums.value,
                    hasBusiness = false,
                    companyDraft = false,
                    forResult = true
                )
            }
        }).show()
    }

    private fun setUpAdapter() {
        adapter = UserRolesRecyclerAdapter(this)
        binding?.recyclerView?.adapter = adapter
        adapter.submitItems(
            arrayListOf(
                UserRoles(
                    title = getString(R.string.i_am_an_business_owner),
                    role = UserRoleEnums.BUSINESS_ROLE.value,
                    selected = MutableLiveData(true)
                ),
                UserRoles(
                    title = getString(R.string.i_am_an_investor),
                    role = UserRoleEnums.INVESTOR_ROLE.value
                ),
                UserRoles(
                    title = getString(R.string.continue_as_visitor),
                    role = UserRoleEnums.VISITOR_ROLE.value
                )
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        InvestorMainActivity.start(this)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as UserRoles
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, UserRolesActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

}