package com.technzone.miniborsa.ui.auth.register.viewmodels

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import co.infinum.goldfinger.Goldfinger
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.data.enums.UserEnums
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.miniborsa.data.models.general.Countries
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.DateTimeUtil
import com.technzone.miniborsa.utils.extensions.checkPhoneNumberFormat
import com.technzone.miniborsa.utils.extensions.millisecondFormatting
import com.technzone.miniborsa.utils.extensions.minToMillisecond
import com.technzone.miniborsa.utils.extensions.secondToMillisecond
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepo: UserRepo,
    @ApplicationContext val context: Context
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
                signUpResendTimer.value = context.resources.getString(R.string.resend_again)
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
        val response = userRepo.resendCode(
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

}