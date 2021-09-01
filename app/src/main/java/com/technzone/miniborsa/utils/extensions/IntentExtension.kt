package com.technzone.miniborsa.utils.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log

fun Context?.openDial(phone: String?) {
    if (phone.isNullOrEmpty()) return
    val phoneNumber = if (phone.startsWith("00") || phone.startsWith("+")) phone else "00$phone"
    val intent = Intent(Intent.ACTION_DIAL)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.data = Uri.parse("tel:${phoneNumber}")
    this?.startActivity(intent)
}

fun Context?.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        intent.data = Uri.parse("http://$url")
    } else {
        intent.data = Uri.parse(url)
    }
    this?.startActivity(intent)
}

fun Context?.openEmail(emailAddress: String?) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("mailto:$emailAddress")
    this?.startActivity(intent)
}

fun Context?.startMapIntent(latitude: Double?, longitude: Double?, dealershipName: String? = null) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=$latitude,$longitude($dealershipName)")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    this?.packageManager?.let { packageManager ->
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }
}

fun Context?.startAppSettingsIntent() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", this?.packageName, null)
    intent.data = uri
    this?.packageManager?.let { packageManager ->
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}

fun Context?.startAppDetailsOnGooglePlay() {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("market://details?id=${this?.packageName}")
    this?.packageManager?.let { packageManager ->
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}

fun Context?.openFacebookPage(facebookPageUrl: String?) {
    if (facebookPageUrl.isNullOrEmpty()) return
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookPageUrl))
        this?.startActivity(intent)
    } catch (e: Exception) {

    }
}

fun Context?.openTwitter(twitterPageUrl: String?) {
    if (twitterPageUrl.isNullOrEmpty()) return

    var intent: Intent?
    try {
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterPageUrl))
        intent.setPackage("com.twitter.android")
    } catch (e: Exception) {
        // no Twitter app, revert to browser
        intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(twitterPageUrl)
        )
    }
    try {
        this?.startActivity(intent)
    } catch (e: Exception) {

    }
}

fun Context?.openInstagram(instagramPageUrl: String?) {
    if (instagramPageUrl.isNullOrEmpty()) return

    try {

        val uri = Uri.parse(instagramPageUrl)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("com.instagram.android")

        this?.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        try {
            this?.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(instagramPageUrl)
                )
            )
        } catch (e: Exception) {

        }

    }
}

fun Activity.navigateToLocation(latitude: Double?, longitude: Double?) {
    val packageName = "com.google.android.apps.maps"
    val query = "google.navigation:q=$latitude,$longitude"

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(query))
    intent.setPackage(packageName)
    startActivity(intent)
}


fun Context?.openShareView(dataWillShare: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, dataWillShare)
    this?.startActivity(Intent.createChooser(intent, "Share with"))
}

fun String?.openWhatsApp(activity: Activity) {
    try {
        val uri =
            Uri.parse("https://api.whatsapp.com/send?phone=" + this.toString())

        val sendIntent = Intent(Intent.ACTION_VIEW, uri)

        activity.startActivity(sendIntent)
    } catch (e: Exception) {
        Log.e("TAG", "ERROR_OPEN_MESSANGER$e")
    }
}