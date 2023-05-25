package com.example.cloudraya.Service

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cloudraya.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        var refreshToken = FirebaseMessaging.getInstance().token
        Log.e("Refresh Token", refreshToken.toString())
    }

    @SuppressLint("MissingPermission")
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Mendapatkan judul dan isi pesan dari RemoteMessage
        val title = message.notification?.title
        val body = message.notification?.body

        //  notifikasi builder
        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.wowrack)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Menampilkan notifikasi
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(0, notificationBuilder.build())

    }

}