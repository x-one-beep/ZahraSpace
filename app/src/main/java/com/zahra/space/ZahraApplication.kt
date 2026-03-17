package com.zahra.space

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZahraApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Channel untuk notifikasi sholat
            val prayerChannel = NotificationChannel(
                "prayer_channel",
                "Waktu Sholat",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi untuk mengingatkan waktu sholat"
                enableLights(true)
                enableVibration(true)
            }
            
            // Channel untuk notifikasi game
            val gameChannel = NotificationChannel(
                "game_channel",
                "Notifikasi Game",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifikasi dari dalam game"
                enableVibration(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(prayerChannel)
            notificationManager.createNotificationChannel(gameChannel)
        }
    }
}
