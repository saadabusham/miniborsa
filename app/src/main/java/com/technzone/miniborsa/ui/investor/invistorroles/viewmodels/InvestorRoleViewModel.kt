package com.technzone.miniborsa.ui.investor.invistorroles.viewmodels

import com.technzone.miniborsa.data.pref.configuration.ConfigurationPref
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InvestorRoleViewModel @Inject constructor(
    private val configurationpref: ConfigurationPref
) : BaseViewModel() {


}