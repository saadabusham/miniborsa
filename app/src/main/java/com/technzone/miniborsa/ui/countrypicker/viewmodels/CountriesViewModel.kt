package com.technzone.miniborsa.ui.countrypicker.viewmodels

import android.content.Context
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val userRepo: UserRepo,
    @ApplicationContext val context: Context
) : BaseViewModel() {


}