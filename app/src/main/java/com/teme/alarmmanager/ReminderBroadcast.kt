package com.teme.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        var builder = NotificationCompat.Builder(context!!, "notification_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Notificare")
            .setContentText("Salut")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        var notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(200,builder.build())
    }
}