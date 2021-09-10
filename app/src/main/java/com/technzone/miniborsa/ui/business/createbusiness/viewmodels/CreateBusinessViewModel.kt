package com.technzone.miniborsa.ui.business.createbusiness.viewmodels

import com.technzone.miniborsa.data.enums.BusinessTypeEnums
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateBusinessViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userPref: UserPref,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
) : BaseViewModel() {
    var businessType: Int = BusinessTypeEnums.BUSINESS_FOR_SALE.value
}