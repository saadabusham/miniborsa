package com.technzone.minibursa.ui.auth.register.viewmodels

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import co.infinum.goldfinger.Goldfinger
import com.technzone.minibursa.R
import com.technzone.minibursa.common.CommonEnums
import com.technzone.minibursa.data.api.response.APIResource
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.data.enums.UserEnums
import com.technzone.minibursa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.minibursa.data.models.general.Countries
import com.technzone.minibursa.data.pref.configuration.ConfigurationPref
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.base.viewmodel.BaseViewModel
import com.technzone.minibursa.utils.DateTimeUtil
import com.technzone.minibursa.utils.extensions.checkPhoneNumberFormat
import com.technzone.minibursa.utils.extensions.millisecondFormatting
import com.technzone.minibursa.utils.extensions.minToMillisecond
import com.technzone.minibursa.utils.extensions.secondToMillisecond
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepo: UserRepo,
    @ApplicationContext val context: Context,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val configurationPref: ConfigurationPref,
) : BaseViewModel() {

    companion object {
        // Phone Number
        const val PHONE_NUMBER_MAX_LENGTH = 9
        const val JORDANIAN_PHONE_NUMBER_WITHOUT_COUNTRY_CODE_REGEX = "^(7|07)(7|8|9)([0-9]{7})\$"

        const val VALIDATION_CODE_LENGTH = 5

        const val RESEND_ENABLE_TIME_IN_MIN: Long = 1
        const val RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND: Long = 1

    }

    val phoneNumberWithoutCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val selectedCountryCode: MutableLiveData<Countries> by lazy { MutableLiveData<Countries>() }
    val firstNameMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val lastNameMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val emailMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val passwordMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val confirmPasswordMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val userIdMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>("") }

    // SignUp Verification Code
    val signUpVerificationCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val signUpResendPinCodeEnabled: MutableLiveData<Boolean>
            by lazy { MutableLiveData<Boolean>(false) }
    val signUpResendTimer: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val forgetCountDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(
            RESEND_ENABLE_TIME_IN_MIN.minToMillisecond(),
            RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND.secondToMillisecond()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                signUpResendTimer.value =
                    millisUntilFinished.millisecondFormatting(DateTimeUtil.TIME_FORMATTING_MIN_AND_SECOND)
            }

            override fun onFinish() {
                signUpResendTimer.value = context.resources.getString(R.string.send_again)
                signUpResendPinCodeEnabled.value = true
            }
        }
    }

    fun startHandleResendSignUpPinCodeTimer() {
        signUpResendPinCodeEnabled.value = false
        forgetCountDownTimer.cancel()
        forgetCountDownTimer.start()
    }

    fun registerUser() = liveData {
        emit(APIResource.loading())
        val response = userRepo.register(
            firstName = firstNameMutableLiveData.value.toString(),
            lastName = lastNameMutableLiveData.value.toString(),
            phoneNumber = selectedCountryCode.value?.code + phoneNumberWithoutCountryCode.value.toString()
                .checkPhoneNumberFormat(),
            email = emailMutableLiveData.value.toString(),
            password = passwordMutableLiveData.value.toString(),
            applicationType = Constants.APPLICATION_TYPE,
            deviceType = Constants.DEVICE_TYPE,
            registrationId = ""
        )
        emit(response)
    }

    fun verifyCode() = liveData {
        emit(APIResource.loading())
        val response = userRepo.verify(
            userIdMutableLiveData.value.toString(),
            signUpVerificationCode.value.toString()
        )
        emit(response)
    }

    fun resendVerificationCode() = liveData {
        emit(APIResource.loading())
        val response = userRepo.forgetPassword(
            emailMutableLiveData.value.toString()
        )
        emit(response)
    }

    fun storeUser(user: UserDetailsResponseModel) {
        signUpVerificationCode.postValue("")
        userRepo.saveUserPassword(passwordMutableLiveData.value ?: "")
        userRepo.setUserStatus(UserEnums.UserState.LoggedIn)
        userRepo.setUser(user)
        user.token?.let { it1 -> userRepo.saveAccessToken(it1) }
    }

    fun isTouchIdShouldVisible(): Boolean {
        return Goldfinger.Builder(context).build().canAuthenticate()
    }

    fun getTermsAndConditions(): String {
        return if (configurationPref.getAppLanguageValue() == CommonEnums.Languages.English.value)
            sharedPreferencesUtil.getConfigurationPreferences()?.configString?.englishTermsAndConditions
                ?: ""
        else sharedPreferencesUtil.getConfigurationPreferences()?.configString?.arabicTermsAndConditions
            ?: ""
    }

}