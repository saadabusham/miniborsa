package com.technzone.miniborsa.utils


import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.technzone.miniborsa.R
import java.util.*

fun Fragment.displayLocationSettingsRequest(activity: Activity, requestCode: Int) {

    val locationRequest = LocationRequest.create()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest.interval = 10000
    locationRequest.fastestInterval = 10000 / 2.toLong()

    LocationServices.getSettingsClient(activity).checkLocationSettings(
        LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest).build()
    ).addOnCompleteListener { task ->
        try {
            task.getResult(ApiException::class.java)
        } catch (exception: ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {

                    try {
                        // Cast to a resolvable exception.
                        val resolvable = exception as (ResolvableApiException)

                        startIntentSenderForResult(
                            resolvable.resolution.intentSender,
                            requestCode,
                            null,
                            0,
                            0,
                            0,
                            null
                        )

                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    } catch (e: ClassCastException) {
                        // Ignore, should be an impossible error.
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }

            }
        }
    }
}

fun Fragment.getLocationName(latitude: Double?, longitude: Double?): String {
    try {

        val address = Geocoder(context, Locale.ENGLISH).getFromLocation(
            latitude ?: 0.0,
            longitude ?: 0.0,
            1
        )
        var locationName = ""
        address?.first()?.let {
            if (it.locality != null) {
                locationName += it.locality + " "
            }
            if (it.subLocality != null) {
                locationName += it.subLocality + " "
            }
            if (it.thoroughfare != null) {
                locationName += it.thoroughfare + " "
            }
            if (it.subThoroughfare != null) {
                locationName += it.subThoroughfare + " "
            }
        }
        return locationName
    } catch (ex: Exception) {
        Log.e("location name", ex.message.toString())
    }

    return ""
}

fun Context.getDistance(fromLocation: Location?, toLocation: Location?): String? {
    val distance: Float = fromLocation?.distanceTo(toLocation) ?: 0f
    val distanceInKM = distance / 1000
    return if (distanceInKM != 0f) {
        java.lang.String.format(Locale.ENGLISH, "%.2f %s", distanceInKM, getString(R.string.km))
    } else {
        null
    }
}
