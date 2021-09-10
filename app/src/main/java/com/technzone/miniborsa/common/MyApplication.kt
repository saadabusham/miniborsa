package com.technzone.miniborsa.common

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.technzone.miniborsa.utils.twilio.chat.client.ChatClientManager
import com.twilio.chat.ErrorInfo
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@HiltAndroidApp
class MyApplication @Inject constructor() : Application() {

    lateinit var chatClientManager: ChatClientManager
    public var deeplink_id = ""
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        instance = this
        chatClientManager = ChatClientManager(applicationContext)
    }
    fun clearTwilio(){
        chatClientManager.shutdown()
    }
    @SuppressLint("RestrictedApi")
    @JvmOverloads
    fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        Handler(Looper.getMainLooper()).post {
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
            toast.show()
        }
    }

    fun showError(error: ErrorInfo) {
        showError("Something went wrong", error)
    }

    fun showError(message: String, error: ErrorInfo) {
        showToast(formatted(message, error), Toast.LENGTH_LONG)
        logErrorInfo(message, error)
    }

    fun logErrorInfo(message: String, error: ErrorInfo) {
        error { formatted(message, error) }
    }

    private fun formatted(message: String, error: ErrorInfo): String {
        return String.format("%s. %s", message, error.toString())
    }

    fun getInstance(): MyApplication {
        return instance
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }

}
