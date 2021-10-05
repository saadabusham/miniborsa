package com.technzone.miniborsa.ui.userrole.viewmodel

import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRolesViewModel @Inject constructor(
        private val userRepo: UserRepo
) : BaseViewModel() {

        fun setUserRole(role:String){
                userRepo.setCurrentRole(role)
        }

}