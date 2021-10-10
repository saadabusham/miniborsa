package com.technzone.minibursa.ui.auth.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.data.pref.configuration.ConfigurationPref
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.LocaleUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val configurationpref: ConfigurationPref
) : BaseViewModel() {

    var languageSettled: Boolean = false

    val englishSelected: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(LocaleUtil.getLanguage() != CommonEnums.Languages.Arabic.value) }

    fun saveLanguage() = liveData {
        configurationpref.setAppLanguageValue(if (englishSelected.value!!)
            CommonEnums.Languages.English.value else CommonEnums.Languages.Arabic.value)
        emit(null)
    }

}