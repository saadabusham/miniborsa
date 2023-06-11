package com.technzone.minibursa.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.technzone.minibursa.data.common.Constants
import com.technzone.minibursa.ui.splash.SplashActivity
import com.technzone.minibursa.utils.pref.SharedPreferencesUtil
import com.technzone.minibursa.R
import com.technzone.minibursa.common.MyApplication
import com.technzone.minibursa.data.repos.user.UserRepo
import com.technzone.minibursa.ui.core.chat.ChatActivity
import com.technzone.minibursa.utils.twilio.chat.services.RegistrationIntentService
import com.twilio.chat.NotificationPayload
import org.jetbrains.anko.startService
import javax.inject.Inject

class FirebaseMessagingService :
    FirebaseMessagingService() {

    @Inject
    lateinit var userRepo: UserRepo

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        startService<RegistrationIntentService>()
    }

    override fun onMessageReceived(data: RemoteMessage) {
        super.onMessageReceived(data)

        // Check if message contains a data payload.

        // Check if message contains a data payload.
        if (data.data.isNotEmpty()) {
            SharedPreferencesUtil.getInstance(applicationContext).setIsNewNotifications(true)
            if (handelChatNotification(data))
                return
            if (!userRepo.getNotificationStatus())
                return
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
    private fun handelChatNotification(remoteMessage: RemoteMessage): Boolean {
        val payload = NotificationPayload(remoteMessage.data)

        val client = MyApplication.instance.chatClientManager.getChatClient()
        client?.handleNotification(payload)

        val type = payload.type

        if (type == NotificationPayload.Type.UNKNOWN) return false

        var title = "Twilio Notification"
        if (type == NotificationPayload.Type.NEW_MESSAGE)
            title = "Twilio: New Message"
        if (type == NotificationPayload.Type.ADDED_TO_CHANNEL)
            title = "Twilio: Added to Channel"
        if (type == NotificationPayload.Type.INVITED_TO_CHANNEL)
            title = "Twilio: Invited to Channel"
        if (type == NotificationPayload.Type.REMOVED_FROM_CHANNEL)
            title = "Twilio: Removed from Channel"

        // Set up action Intent
        val intent = Intent(this, ChatActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val cSid = payload.channelSid
        if (!"".contentEquals(cSid)) {
            intent.putExtra(Constants.Twilio.CHAT_SID, cSid)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(
            this, /*NotificationChannel.DEFAULT_CHANNEL_ID*/
            "miscellaneous"
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(payload.body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(Color.rgb(214, 10, 37))
            .build()

        val soundFileName = payload.sound
        if (resources.getIdentifier(soundFileName, "raw", packageName) != 0) {
            val sound = Uri.parse("android.resource://$packageName/raw/$soundFileName")
            notification.defaults = notification.defaults and Notification.DEFAULT_SOUND.inv()
            notification.sound = sound
        } else {
            notification.defaults = notification.defaults or Notification.DEFAULT_SOUND
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notification)
        return true
    }
}