package com.technzone.miniborsa.data.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.technzone.miniborsa.data.common.Constants
import com.technzone.miniborsa.ui.splash.SplashActivity
import com.technzone.miniborsa.utils.pref.SharedPreferencesUtil
import com.twilio.voice.*
import com.technzone.miniborsa.R

class FirebaseMessagingService :
    FirebaseMessagingService() {


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(data: RemoteMessage) {
        super.onMessageReceived(data)

        // Check if message contains a data payload.

        // Check if message contains a data payload.
        if (data.data.isNotEmpty()) {
            SharedPreferencesUtil.getInstance(applicationContext).setIsNewNotifications(true)
            val message = data.data

            val intent = Intent(this, SplashActivity::class.java)
//            intent.putExtra(EXTRA_FROM_NOTIFICATION, true)
//            if (data.data.containsKey("type"))
//                intent.putExtra(EXTRA_FROM_NOTIFICATION_TYPE, data.data["type"])
//            if (data.data.containsKey("flightId"))
//                intent.putExtra(EXTRA_FROM_NOTIFICATION_FLIGHT_ID, data.data["flightId"])
//            if (data.data.containsKey("bookingId"))
//                intent.putExtra(EXTRA_FROM_NOTIFICATION_BOOKING_ID, data.data["bookingId"])

            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val channelId = data.notification?.channelId ?: ""
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message[getString(R.string.notification_title_key)])
                .setContentText(message[getString(R.string.notification_message_key)])
                .setShowWhen(true)
                .setAutoCancel(true).setContentIntent(pendingIntent)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    Constants.NotificationsChannels.DEFAULT_CHANNEL,
                    NotificationManager.IMPORTANCE_HIGH
                )
                manager.createNotificationChannel(notificationChannel)
            }

            with(NotificationManagerCompat.from(this)) {
                notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
            }
        }

    }
}