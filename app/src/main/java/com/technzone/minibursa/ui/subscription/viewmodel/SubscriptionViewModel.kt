package com.technzone.minibursa.ui.subscription.viewmodel

import com.technzone.minibursa.data.pref.configuration.ConfigurationPref
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val configurationpref: ConfigurationPref
) : BaseViewModel() {


}