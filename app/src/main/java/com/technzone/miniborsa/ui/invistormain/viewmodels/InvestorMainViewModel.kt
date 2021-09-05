package com.technzone.miniborsa.ui.invistormain.viewmodels

import com.technzone.miniborsa.data.repos.auth.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorMainViewModel @Inject constructor(
    val userRepo: UserRepo
) : BaseViewModel() {

}