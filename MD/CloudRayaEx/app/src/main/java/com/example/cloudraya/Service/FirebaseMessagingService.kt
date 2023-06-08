package com.example.cloudraya.Service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ApplicationProvider
import com.example.cloudraya.Local.NotifDao
import com.example.cloudraya.Local.NotifData
import com.example.cloudraya.Local.NotifDatabase
import com.example.cloudraya.MainActivity
import com.example.cloudraya.ModelApiTest.NotificationsData
import com.example.cloudraya.R
import com.example.cloudraya.Verify.VerifyActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.launch
import kotlin.random.Random

class FirebaseMessagingService : FirebaseMessagingService() {
    lateinit var notifDao: NotifDao
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

        val customData = message.data

        val vmId = customData["vm_id"]!!
        val action = customData["action"]!!

        val dataNotif = NotifData(0,title!!,body!!,vmId, action)

        notifDao = NotifDatabase.getDatabase(this)!!.notifDao()
        addNotif(dataNotif)

        showNotification(title!!,body!!, vmId, action)



    }
    fun addNotif(notifData: NotifData){
        notifDao.insert(notifData)

    }
    @SuppressLint("RemoteViewLayout")
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews {
        val remoteViews = RemoteViews(
            "com.example.cloudraya",
            R.layout.notification
        )
        remoteViews.setTextViewText(R.id.tvTitle, title)
        remoteViews.setTextViewText(R.id.body, message)
        remoteViews.setImageViewResource(
            R.id.icon,
            R.drawable.wowcrck
        )
        return remoteViews
    }

    // Method to display the notifications
    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(title: String, message: String, vmId: String, action: String) {

        // Pass the intent to switch to the MainActivity
        val intent = Intent(this@FirebaseMessagingService, VerifyActivity::class.java)
        intent.putExtra("vmId", vmId)
        intent.putExtra("action", action)

        val channel_id = "notification_channel"

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var requestCode = Random.nextInt()
        val pendingIntent = PendingIntent.getActivity(
            this, requestCode, intent,
            PendingIntent.FLAG_MUTABLE
        )

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            channel_id
        )
            .setSmallIcon(R.drawable.wowcrck)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(
            getCustomDesign(title, message)
        )

        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager?
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channel_id, "web_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager!!.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager!!.notify(0, builder.build())
    }


}