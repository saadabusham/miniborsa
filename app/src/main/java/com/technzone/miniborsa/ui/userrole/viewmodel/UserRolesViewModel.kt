package com.technzone.miniborsa.ui.userrole.viewmodel

import com.technzone.miniborsa.data.repos.configuration.ConfigurationRepo
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRolesViewModel @Inject constructor(
        private val userRepo: UserRepo
) : BaseViewModel() {

        fun setUserRole(role:Int){
                userRepo.setCurrentRole(role)
        }

}