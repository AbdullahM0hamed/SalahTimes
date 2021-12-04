package com.prayer.times.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.prayer.times.Constants
import com.prayer.times.R
import com.prayer.times.worker.NotificationWorker

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.getAction() == Constants.SET_PRAYER_REMINDER) {
            val prayerAlarmRequest: WorkRequest =
                OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                    .setInputData(
                        workDataOf(
                            Constants.SALAH_NAME to getName(context, intent.getStringExtra(Constants.SALAH_NAME))
                        )
                    )
                    .build()

            WorkManager
                .getInstance(context)
                .enqueue(prayerAlarmRequest)
        }
    }

    fun getName(context: Context, name: String?) = when (name) {
        "FAJR" -> context.getString(R.string.fajr)
        "DHUHR" -> context.getString(R.string.dhuhr)
        "ASR" -> context.getString(R.string.asr)
        "MAGHRIB" -> context.getString(R.string.maghrib)
        "ISHA" -> context.getString(R.string.isha)
        else -> ""
    }
}
