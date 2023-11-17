package com.bonghwan.mosquito.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bonghwan.mosquito.R
import com.bonghwan.mosquito.data.api.dto.ReqFcmToken
import com.bonghwan.mosquito.data.api.provideFcmApi
import com.bonghwan.mosquito.ui.intro.IntroActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        uploadToken(ReqFcmToken(token))
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCM Message", "From: ${message.from}")
        Log.d("FCM Message", "Notification Message Body: ${message.notification?.body}")

        message.notification?.let {
            // 알림을 만들어 표시
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val channelId = "YourChannelId" // 알림 채널 ID

        val intent = Intent(this, IntroActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.splash_mosquito) // 알림 아이콘
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "YourChannelName",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // 알림을 표시
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun uploadToken(reqFcmToken: ReqFcmToken) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fcmApi = provideFcmApi()
                fcmApi.createFcmToken(reqFcmToken).execute()
            } catch (e: IOException) {
                Log.e("IOException", e.message.toString())
            }
        }
    }
}