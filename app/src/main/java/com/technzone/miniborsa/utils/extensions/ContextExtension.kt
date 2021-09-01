package com.technzone.miniborsa.utils.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.technzone.miniborsa.utils.pref.PrefConstants.APP_LANGUAGE_VALUE
import com.technzone.miniborsa.common.CommonEnums
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import java.util.*

fun Context?.longToast(toastMessage: String?) {
    if (toastMessage.isNullOrEmpty()) return
    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
}

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context?.shortToast(toastMessage: String?) {
    if (toastMessage.isNullOrEmpty()) return
    Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(toastMessage: String?) {
    if (toastMessage.isNullOrEmpty()) return
    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_LONG).show()
}

fun Fragment.shortToast(toastMessage: String?) {
    if (toastMessage.isNullOrEmpty()) return
    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
}


fun Context?.copyText(textWillBeCopy: String) {
    this?.let {
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", textWillBeCopy)
        if (clipboard == null || clip == null) return
        clipboard.setPrimaryClip(clip)
    }
}

fun Context.updateLanguage() {
    val local = Locale(
        SharedPreferencesUtil.getInstance(this)
            .getStringPreferences(APP_LANGUAGE_VALUE, CommonEnums.Languages.English.value)
    )
    Locale.setDefault(local)
    val configuration: Configuration = this.resources.configuration
    configuration.setLocale(local)
    this.resources.updateConfiguration(configuration, this.resources.displayMetrics)
}

fun Uri?.openInBrowser(context: Context) {
    this ?: return // Do nothing if uri is null

    val browserIntent = Intent(Intent.ACTION_VIEW, this)
    ContextCompat.startActivity(context, browserIntent, null)
}

fun String?.asUri(): Uri? {
    try {
        return Uri.parse(this)
    } catch (e: Exception) {
    }
    return null
}

fun Activity.refreshLocal() {
    try {

        val language = SharedPreferencesUtil.getInstance(this)
            .getStringPreferences(APP_LANGUAGE_VALUE, CommonEnums.Languages.English.value)

        updateLanguage()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (language == CommonEnums.Languages.English.value) {
                this.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
            } else {
                this.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

            }
        }

    } catch (ignore: Exception) {

    }
}

fun Context?.deviceIsConnectedToInternet(): Boolean {
    val netInfo =
        (this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

    if (netInfo != null) {
        if (netInfo.isConnected) {
            return true
        }
    }
    return false
}


