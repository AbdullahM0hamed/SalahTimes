package com.prayer.times.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ListenableWorker.Result
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.prayer.times.Constants
import com.prayer.times.R
import com.prayer.times.calculation.Calculation

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val channelId = "${context.packageName}-adhan"
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val context = context
    private var name: String? = null

    override fun doWork(): Result {
        name = inputData.getString(Constants.SALAH_NAME)
        createNotification()
        Calculation(context).setNextPrayerAlarm()

        return Result.success()
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, context.getString(R.string.notification_adhan), NotificationManager.IMPORTANCE_HIGH)
            channel.description = context.getString(R.string.notification_description)
            channel.setSound(
                Uri.parse("android.resource://${context.packageName}/${R.raw.adhan}"),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification() {
        createNotificationChannel()
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_times_fill_24dp)
            setContentTitle(context.getString(R.string.prayer_reminder, name))
            setAutoCancel(true)
            setSound(Uri.parse("android.resource://${context.packageName}/${R.raw.adhan}"))

            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        notificationManager.notify(1000, notificationBuilder.build())
    }
}
