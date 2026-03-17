package com.zahra.space.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.zahra.space.MainActivity
import com.zahra.space.R
import java.util.*
import java.util.concurrent.TimeUnit

class PrayerNotificationService : Service() {
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "prayer_channel"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        schedulePrayerNotifications()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Notifikasi Waktu Sholat",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Mengingatkan waktu sholat"
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Zahra Space")
            .setContentText("Layanan notifikasi sholat aktif")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
    
    private fun schedulePrayerNotifications() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Prayer times (example for Jakarta)
        val prayerTimes = mapOf(
            "Subuh" to 4 * 60 + 30,
            "Dzuhur" to 12 * 60,
            "Ashar" to 15 * 60 + 20,
            "Maghrib" to 18 * 60,
            "Isya" to 19 * 60 + 30
        )
        
        val calendar = Calendar.getInstance()
        
        prayerTimes.forEach { (prayer, minuteOfDay) ->
            val prayerCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, minuteOfDay / 60)
                set(Calendar.MINUTE, minuteOfDay % 60)
                set(Calendar.SECOND, 0)
            }
            
            // If time has passed today, schedule for tomorrow
            if (prayerCalendar.timeInMillis < System.currentTimeMillis()) {
                prayerCalendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            
            val intent = Intent(this, PrayerAlarmReceiver::class.java).apply {
                putExtra("prayer_name", prayer)
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                prayer.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    prayerCalendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    prayerCalendar.timeInMillis,
                    pendingIntent
                )
            }
        }
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
