package com.technzone.minibursa.utils

import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.util.*

class LocationUtil {

    companion object {
        @JvmStatic
        fun getLocationName(context: Context, location: Location): String {
            val geocode = Geocoder(context, Locale.getDefault())
            val names = geocode.getFromLocation(location.latitude, location.longitude, 1)[0]
            return names.subLocality.plus(",").plus(names.locality).plus(",")
                .plus(names.countryName)
        }

        @JvmStatic
        fun getLocationName(context: Context, latLng: LatLng): String {
            return try {
                val geocode = Geocoder(context, Locale.getDefault())
                val names = geocode.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
                names.subLocality.plus(",").plus(names.locality).plus(",")
                    .plus(names.countryName)
            } catch (ignore: Exception) {
                ""
            }
        }

        @JvmStatic
        fun getDistanceInKM(firstLocation: Location, secondLocation: Location) =
            firstLocation.distanceTo(secondLocation).div(1000)
    }
}