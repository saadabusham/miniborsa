package com.technzone.miniborsa.ui.subscription.viewmodel

import com.technzone.miniborsa.data.pref.configuration.ConfigurationPref
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val configurationpref: ConfigurationPref
) : BaseViewModel() {


}