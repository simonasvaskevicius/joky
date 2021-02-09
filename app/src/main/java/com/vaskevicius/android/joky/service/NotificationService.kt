package com.vaskevicius.android.joky.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vaskevicius.android.joky.R
import com.vaskevicius.android.joky.ui.joke.JokeActivity

//notifications sent with Firebase Cloud messaging

class NotificationService : FirebaseMessagingService() {


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        // based upon that message from console, we setup a notification
        setUpNotification(p0?.notification)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    private fun setUpNotification(notification: RemoteMessage.Notification?) {
        var style: NotificationCompat.BigTextStyle = NotificationCompat.BigTextStyle()
        style.setSummaryText(notification?.body)

        val channelID = getString(R.string.default_notification_channel_id)
        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification?.title)
            .setContentText(notification?.body)
            .setContentIntent(getPendingIntent())
            .setSound(ringtone).setAutoCancel(true).setStyle(style)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // create notification channel for Oreo and above version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                notification?.title,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        // trigger the notification
        notificationManager.notify(0, notificationBuilder.build())
    }

    //intent for opening jokeActivity, when notification clicked
    private fun getPendingIntent(): PendingIntent = PendingIntent.getActivity(
        this, 0,
        Intent(
            this,
            JokeActivity::class.java
        ), 0
    )

}