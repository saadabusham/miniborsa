package com.technzone.minibursa.utils


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
import com.google.android.gms.maps.model.LatLng
import com.technzone.minibursa.R
import com.technzone.minibursa.data.models.map.Address
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

fun Fragment.getLocationAddress(latitude: Double?, longitude: Double?): Address {
    try {

        val address = Geocoder(context, Locale.ENGLISH).getFromLocation(
            latitude ?: 0.0,
            longitude ?: 0.0,
            1
        )
        var addressModel: Address = Address()
        var locationName = ""
        address?.first()?.let {

            if (it.countryName != null) {
                locationName += it.countryName + ","
                addressModel.country = it.countryName
            }
            if (it.locality != null) {
                addressModel.city = it.locality
                locationName += it.locality + ","
            }
            if (it.subLocality != null) {
                locationName += it.subLocality + ","
            }
            if (it.thoroughfare != null) {
                locationName += it.thoroughfare + ","
            }
            if (it.subThoroughfare != null) {
                locationName += it.subThoroughfare + ","
            }
        }
        addressModel.locationName = locationName
        return addressModel
    } catch (ex: Exception) {
        Log.e("location name", ex.message.toString())
    }

    return Address()
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

fun Activity.displayLocationSettingsRequest(requestCode: Int) {

    val locationRequest = LocationRequest.create()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest.interval = 10000
    locationRequest.fastestInterval = 10000 / 2.toLong()

    LocationServices.getSettingsClient(this).checkLocationSettings(
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

fun Context.getDistanceInKM(fromLocation: Location?, toLocation: Location?): Float? {
    val distance: Float = fromLocation?.distanceTo(toLocation) ?: 0f
    return distance / 1000
}

fun Context.getDistanceInKM(fromLocation: LatLng?, toLocation: LatLng?): Float? {
    val location1 = Location("")
    location1.latitude = fromLocation?.latitude ?: 0.0
    location1.longitude = fromLocation?.longitude ?: 0.0

    val location2 = Location("")
    location2.latitude = toLocation?.latitude ?: 0.0
    location2.longitude = toLocation?.longitude ?: 0.0

    val distance: Float = location1.distanceTo(location2)
    return distance / 1000
}

fun Context.getLocationName(latitude: Double?, longitude: Double?): String {
    try {

        val address = Geocoder(this, Locale.ENGLISH).getFromLocation(
            latitude ?: 0.0,
            longitude ?: 0.0,
            1
        )
        var locationName = ""
        address?.first()?.let {
            if (it.locality != null) {
                locationName += it.locality + ","
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

