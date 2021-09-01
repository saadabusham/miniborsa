package com.technzone.miniborsa.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import kotlin.system.exitProcess

class LogoutUtil {

    companion object {
        fun restartApp(activity: Activity) {
            // Systems at 29/Q and later don't allow relaunch, but System.exit(0) on
            // all supported systems will relaunch ... but by killing the process, then
            // restarting the process with the back stack intact. We must make sure that
            // the launch activity is the only thing in the back stack before exiting.
            val pm: PackageManager = activity.packageManager
           val  intent: Intent? = pm.getLaunchIntentForPackage (activity.packageName)
            activity.finishAffinity() // Finishes all activities.
            activity.startActivity(intent)    // Start the launch activity
            exitProcess(0)    // System finishes and automatically relaunches us.
        }
    }
}