package com.example.apstelegrammessenger

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class StayAliveService: Service() {
    private val notificationID = 4000
    private lateinit var notification: Notification
    private val channelID = "APS Telegram"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        notification = getNotification()
        startService()
    }

    private fun startService() {
        try {
            startForeground(notificationID, notification)
            updateNotification()
        } catch (e: Exception) {
            println("Error starting foreground service: $e")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        startService()
        return Service.START_STICKY
    }

    private fun getNotification(): Notification {
        val builder = NotificationCompat.Builder(this, channelID)
        builder.setOngoing(true)
        builder.setOnlyAlertOnce(true)
        builder.setCategory(NotificationCompat.CATEGORY_STATUS)
        builder.setSmallIcon(androidx.core.R.drawable.notification_icon_background)
        builder.setContentTitle("APS Telegram")
        builder.setContentText("APS Telegram Notification")
        return builder.build()
    }

    private fun updateNotification() {
        val mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelID, channelID as CharSequence, NotificationManager.IMPORTANCE_HIGH)
        mNotificationManager.createNotificationChannel(channel)
    }
}