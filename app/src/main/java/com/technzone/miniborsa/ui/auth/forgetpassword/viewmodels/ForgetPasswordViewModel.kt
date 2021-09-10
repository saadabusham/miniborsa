package com.technzone.miniborsa.ui.auth.forgetpassword.viewmodels

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.technzone.miniborsa.R
import com.technzone.miniborsa.data.api.response.APIResource
import com.technzone.miniborsa.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.miniborsa.data.pref.user.UserPref
import com.technzone.miniborsa.data.repos.user.UserRepo
import com.technzone.miniborsa.ui.base.viewmodel.BaseViewModel
import com.technzone.miniborsa.utils.DateTimeUtil
import com.technzone.miniborsa.utils.extensions.millisecondFormatting
import com.technzone.miniborsa.utils.extensions.minToMillisecond
import com.technzone.miniborsa.utils.extensions.secondToMillisecond
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val userPref: UserPref,
    @ApplicationContext context: Context
) : BaseViewModel() {

    companion object {

        const val VALIDATION_CODE_LENGTH = 5

        const val RESEND_ENABLE_TIME_IN_MIN: Long = 1
        const val RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND: Long = 1
        const val PHONE_NUMBER_MAX_LENGTH = 9
    }

    val email: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val selectedCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val passwordMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val confirmPasswordMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    // Forget Password Verification Code
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
        if (response.statusSubCode == ResponseSubErrorsCodeEnum.Success) {
            signUpVerificationCode.postValue("")
            userPref.saveAccessToken(response.data?.data?.token!!)
        }
        emit(response)
    }

    fun resendVerificationCode() = liveData {
        emit(APIResource.loading())
        val response = userRepo.forgetPassword(
            email.value.toString()
        )
        emit(response)
    }

    fun recoveryPassword() = liveData {
        emit(APIResource.loading())
        val response = userRepo.resetPassword(
            passwordMutableLiveData.value.toString()
        )
        emit(response)
    }


}