package com.prayer.times.calculation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.prayer.times.Constants
import com.prayer.times.preference.PreferencesHelper
import com.prayer.times.receiver.AlarmReceiver
import com.prayer.times.ui.MainActivity
import java.util.Calendar
import java.util.Date

class Calculation(
    val context: Context
) {

    private val helper = PreferencesHelper(context)
    private val latitude = helper.getString(Constants.LATITUDE)!!.toDouble()
    private val longitude = helper.getString(Constants.LONGITUDE)!!.toDouble()

    data class Salah(
        val name: String,
        val time: Date
    )

    fun getPrayerTimes(day: Date?): PrayerTimes {
        val coordinates = Coordinates(latitude, longitude)
        val date = DateComponents.from(day)

        val params = CalculationMethod.NORTH_AMERICA.getParameters()
        params.madhab = Madhab.SHAFI

        return PrayerTimes(coordinates, date, params)
    }

    fun getNextPrayer(): Salah {
        val calendar = Calendar.getInstance()
        var times = getPrayerTimes(calendar.time)

        var nextPrayer = times.nextPrayer()
        when (nextPrayer.name) {
            "NONE" -> {
                calendar.add(Calendar.DATE, 1)
                times = getPrayerTimes(calendar.time)
                nextPrayer = times.currentPrayer(times.fajr)
            }
            "SUNRISE" -> {
                nextPrayer = times.nextPrayer(times.sunrise)
            }
        }

        return Salah(nextPrayer.name, times.timeForPrayer(nextPrayer))
    }

    fun setNextPrayerAlarm() {
        val nextPrayer = getNextPrayer()
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            action = Constants.SET_PRAYER_REMINDER
            putExtra(Constants.SALAH_NAME, nextPrayer.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(nextPrayer.time.getTime(), pendingIntent), pendingIntent)
    }
}
