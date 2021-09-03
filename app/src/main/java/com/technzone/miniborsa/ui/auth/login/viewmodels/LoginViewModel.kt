package com.technzone.miniborsa.ui.auth.login.viewmodels

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.enums.UserEnums
import com.technzone.miniborsa.data.models.auth.login.UserDetailsResponseModel
import com.technzone.miniborsa.data.models.configuration.UpdateStatus
import com.technzone.miniborsa.data.repos.auth.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.DateTimeUtil
import com.technzone.miniborsa.utils.extensions.millisecondFormatting
import com.technzone.miniborsa.utils.extensions.minToMillisecond
import com.technzone.miniborsa.utils.extensions.secondToMillisecond
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    @ApplicationContext context: Context
) : BaseViewModel() {

    companion object {

        const val VALIDATION_CODE_LENGTH = 5

        const val RESEND_ENABLE_TIME_IN_MIN: Long = 1
        const val RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND: Long = 1
        const val JORDANIAN_PHONE_NUMBER_WITHOUT_COUNTRY_CODE_REGEX = "^(7|07)(7|8|9)([0-9]{7})\$"

    }

    fun getTouchIdStatus(): Boolean {
        return userRepo.getTouchIdStatus()
    }

    fun getUpdateStatus(): UpdateStatus {
        return sharedPreferencesUtil.getConfigurationPreferences()?.updateStatus ?: UpdateStatus()
    }

    fun checkIfThereUpdate(): Boolean {
//        return sharedPreferencesUtil.getConfigurationPreferences()?.updateStatus?.version != BuildConfig.VERSION_NAME
        return false
    }

    fun checkIsUpdateMandatory(): Boolean {
        return sharedPreferencesUtil.getConfigurationPreferences()?.updateStatus?.isMandatory
                ?: false
    }


    fun loadPhoneNumberLocal(): String {
        return userRepo.getUser()?.phoneNumber ?: ""
    }

    fun loadPasswordLocal(): String {
        return userRepo.getUserPassword()
    }

    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val passwordMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    // Login Verification Code
    val signUpVerificationCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val signUpResendPinCodeEnabled: MutableLiveData<Boolean>
            by lazy { MutableLiveData<Boolean>(false) }
    val signUpResendTimer: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val userIdMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val forgetCountDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(
                RESEND_ENABLE_TIME_IN_MIN.minToMillisecond(),
                RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND.secondToMillisecond()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                signUpResendTimer.value =
                        millisUntilFinished.millisecondFormatting(DateTimeUtil.TIME_FORMATTING_MIN_AND_SECOND)
            }

            override fun onFinish() {
                signUpResendPinCodeEnabled.value = true
                signUpResendTimer.value = context.resources.getString(R.string.resend_again)
            }
        }
    }

    fun loginUser(phoneNumber: String, password: String) = liveData {
        emit(APIResource.loading())
        val response = userRepo.login(
                phoneNumber,
                password
        )
        emit(response)
    }


    fun storeUser(user: UserDetailsResponseModel) {
        signUpVerificationCode.postValue("")
        if (loadPasswordLocal().isNullOrEmpty())
            userRepo.saveUserPassword(passwordMutableLiveData.value?:"")
        user.token?.let { userRepo.saveAccessToken(it) }
        userRepo.setUserStatus(UserEnums.UserState.LoggedIn)
        userRepo.setUser(user)
    }

    fun startHandleResendSignUpPinCodeTimer() {
        signUpResendPinCodeEnabled.value = false
        forgetCountDownTimer.cancel()
        forgetCountDownTimer.start()
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
            email.value.toString()
        )
        emit(response)
    }
}