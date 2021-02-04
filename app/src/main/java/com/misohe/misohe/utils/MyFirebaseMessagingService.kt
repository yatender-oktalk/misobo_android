package com.misohe.misohe.utils

import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.misohe.misohe.R
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
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_misobo_cloud))
                .setSmallIcon(R.drawable.ic_misobo_cloud)
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