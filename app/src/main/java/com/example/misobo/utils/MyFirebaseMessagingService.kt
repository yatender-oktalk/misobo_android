package com.example.misobo.utils

import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.misobo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //Log.d(TAG, "From: ${remoteMessage?.from}")
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("Message", "Message Notification Body: ${it.body}")
            val notification = NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.from)
                .setContentText(it.body)
                .setSmallIcon(R.drawable.misobo_icon)
                .build()
            val manager = NotificationManagerCompat.from(applicationContext)
            manager.notify(/*notification id*/0, notification)
            //Message Services handle notification

        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }
}