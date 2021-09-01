package com.technzone.miniborsa.utils.extensions

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
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