package com.technzone.minibursa.ui.userrole.viewmodel

import androidx.lifecycle.liveData
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.repos.business.BusinessRepo
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRolesViewModel @Inject constructor(
        private val userRepo: UserRepo,
        private val businessRepo: BusinessRepo
) : BaseViewModel() {

        fun setUserRole(role:String){
                userRepo.setCurrentRole(role)
        }

        fun getPendingListing() = liveData {
                emit(APIResource.loading())
                val response =
                        businessRepo.getRequestCompany()
                emit(response)
        }
}