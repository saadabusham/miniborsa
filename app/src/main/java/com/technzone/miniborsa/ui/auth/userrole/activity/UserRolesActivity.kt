package com.technzone.miniborsa.ui.auth.userrole.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.enums.UserRoleEnums
import com.technzone.miniborsa.data.models.auth.login.UserRoles
import com.technzone.miniborsa.databinding.ActivityUserRoleBinding
import com.technzone.miniborsa.ui.auth.userrole.adapters.UserRolesRecyclerAdapter
import com.technzone.miniborsa.ui.auth.userrole.viewmodel.UserRolesViewModel
import com.technzone.miniborsa.ui.base.activity.BaseBindingActivity
import com.technzone.miniborsa.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.miniborsa.ui.general.adapters.ChooseGeneralRecyclerAdapter
import com.technzone.miniborsa.ui.general.adapters.SelectedGeneralRecyclerAdapter
import com.technzone.miniborsa.utils.extensions.showErrorAlert
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

            } ?: also {
                showErrorAlert(getString(R.string.user_role),
                        getString(R.string.please_select_your_role))
            }
        }
    }

    private fun setUpAdapter() {
        adapter = UserRolesRecyclerAdapter(this)
        binding?.recyclerView?.adapter = adapter
        adapter.submitItems(
                arrayListOf(
                        UserRoles(
                                name = getString(R.string.i_am_an_business_owner),
                                role = UserRoleEnums.BUSINESS_ROLE.value,
                                selected = MutableLiveData(true)
                        ),
                        UserRoles(
                                name = getString(R.string.i_am_an_investor),
                                role = UserRoleEnums.INVESTOR_ROLE.value
                        ),
                        UserRoles(
                                name = getString(R.string.continue_as_visitor),
                                role = UserRoleEnums.VISITOR_ROLE.value
                        )
                )
        )
    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, UserRolesActivity::class.java)
            context?.startActivity(intent)
        }

    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        item as UserRoles
        when(item.role){
            UserRoleEnums.BUSINESS_ROLE.value -> {

            }
            UserRoleEnums.INVESTOR_ROLE.value -> {

            }
            UserRoleEnums.VISITOR_ROLE.value -> {

            }
        }
    }

}