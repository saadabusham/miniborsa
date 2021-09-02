package com.technzone.miniborsa.ui.auth.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.common.CommonEnums
import com.technzone.miniborsa.data.pref.configuration.ConfigurationPref
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val configurationpref: ConfigurationPref
) : BaseViewModel() {

    var languageSettled: Boolean = false
    val englishSelected: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(true) }
    fun onEnglishClicked() {
        englishSelected.postValue(true)
    }

    fun onArabicClicked() {
        englishSelected.postValue(false)
    }
    fun getIsLanguageSelected(lang: String): Boolean {
        return configurationpref.getAppLanguageValue() == lang
    }

    fun saveLanguage(language: String) = liveData {
        configurationpref.setAppLanguageValue(language)
        languageSettled = true
        emit(null)
    }


}