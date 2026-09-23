package com.ethiopianorthodox.prayeralarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        val channelId = "prayer_bell"

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= 26) {

            val channel = NotificationChannel(
                channelId,
                "Prayer Bell",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.setSound(
                RingtoneManager.getDefaultUri(
                    RingtoneManager.TYPE_NOTIFICATION
                ),
                null
            )

            notificationManager.createNotificationChannel(channel)
        }

        val prayerName =
            intent.getStringExtra("name")
                ?: "የጸሎት ጊዜ"

        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(
                    android.R.drawable.ic_lock_idle_alarm
                )
                .setContentTitle(
                    "🔔 $prayerName"
                )
                .setContentText(
                    "የጸሎት ጊዜ ደርሷል"
                )
                .setPriority(
                    NotificationCompat.PRIORITY_MAX
                )
                .setAutoCancel(true)
                .build()

        notificationManager.notify(
            2000 + intent.getIntExtra("id", 0),
            notification
        )

        // Schedule the next day's prayer alarms.
        AlarmScheduler.scheduleAll(context)
    }
}
