package com.technzone.minibursa.utils.extensions

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener


fun Activity?.requestPermission(
    permission: String?,
    listener: MySinglePermissionListeners?,
    reshowWhenUserDeniedRequest: Boolean = false
) {
    Dexter.withActivity(this)
        .withPermission(permission)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                listener?.onSinglePermissionGranted(response?.permissionName)
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {
                if (reshowWhenUserDeniedRequest) {
                    token?.continuePermissionRequest()
                }
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                listener?.onSinglePermissionDenied(response?.permissionName)
            }
        }).check()
}

fun Activity?.requestLocationPermission(
    listener: MyMultiPermissionListeners?,
    reshowWhenUserDeniedRequest: Boolean = false
) {

    Dexter.withActivity(this)
        .withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.grantedPermissionResponses.size == 2) {
                    listener?.onPermissionGranted()
                } else {
                    listener?.onPermissionDenied()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {
                if (reshowWhenUserDeniedRequest) {
                    token?.continuePermissionRequest()
                }
            }
        }).check()
}

interface MyMultiPermissionListeners {
    /**
     * This Method Called When User Allow Single Permission
     */
    fun onPermissionGranted()

    /**
     * This Method Called When User Click Denied and check don't ask me again
     */
    fun onPermissionDenied()

}

interface MySinglePermissionListeners {
    /**
     * This Method Called When User Allow Single Permission
     */
    fun onSinglePermissionGranted(permission: String?)

    /**
     * This Method Called When User Click Denied and check don't ask me again
     */
    fun onSinglePermissionDenied(permissionName: String?)
}