package com.prayer.times.ui.controller

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.batoulapps.adhan.SunnahTimes
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.prayer.times.Constants
import com.prayer.times.R
import com.prayer.times.calculation.Calculation
import com.prayer.times.databinding.TimesLayoutBinding
import com.prayer.times.location.Location
import com.prayer.times.preference.PreferencesHelper
import com.prayer.times.ui.MainActivity
import com.prayer.times.ui.action.AddToDate
import com.prayer.times.ui.action.UpdateTimes
import com.prayer.times.ui.controller.base.BaseController
import com.prayer.times.ui.mainStore
import com.prayer.times.ui.state.SalahTimesState
import org.reduxkotlin.StoreSubscription
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class TimesController : BaseController<TimesLayoutBinding>() {

    private lateinit var storeSubscription: StoreSubscription
    private val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy")

    override fun onActivityResumed(activity: Activity) {
        if ((activity as MainActivity).isLocationStored()) {
            mainStore.dispatch(UpdateTimes())
        }
    }

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        binding = TimesLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        storeSubscription = mainStore.subscribe { newState(mainStore.state.salahTimesState) }

        if ((activity as MainActivity).isLocationStored()) {
            mainStore.dispatch(UpdateTimes())
        }

        binding.dayBefore.setOnClickListener {
            mainStore.dispatch(AddToDate(-1))
        }

        binding.dayAfter.setOnClickListener {
            mainStore.dispatch(AddToDate(1))
        }
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)

        //Unsubscribe
        storeSubscription()
    }

    fun getHijriDate(date: Date): String {
        val hijriCalendar = UmmalquraCalendar()
        val localFormatter = SimpleDateFormat("EEEE, dd MMMM yyyy")

        hijriCalendar.get(Calendar.YEAR)
        hijriCalendar.get(Calendar.MONTH)
        hijriCalendar.get(Calendar.DAY_OF_MONTH)

        localFormatter.setCalendar(hijriCalendar)
        return localFormatter.format(date)
    }

    fun newState(state: SalahTimesState) {
        val formattedDate = formatter.format(state.date)
        val hijriDate = getHijriDate(state.date)

        binding.hijriDate.text = hijriDate
        binding.date.text = formattedDate

        val calculator = Calculation(activity as Context)
        val nextPrayerText = (activity as Context).getString(R.string.next_prayer, getTimeFormat().format(calculator.getNextPrayer().time))
        binding.nextPrayer.text = nextPrayerText

        val times = calculator.getPrayerTimes(state.date)

        val timeBindings = listOf(binding.fajrStart, binding.sunriseStart, binding.dhuhrStart, binding.asrStart, binding.maghribStart, binding.ishaStart, binding.midnightStart)
        val timeDates = listOf(times.fajr, times.sunrise, times.dhuhr, times.asr, times.maghrib, times.isha, SunnahTimes(times).middleOfTheNight)

        timeBindings.zip(timeDates) { view, time ->
            view.text = getTimeFormat().format(time)
        }
    }

    fun getTimeFormat(): SimpleDateFormat {
        val helper = PreferencesHelper(activity as Context)
        val format = if (helper.getString(Constants.TIME_FORMAT_KEY) ?: Constants.TIME_FORMAT_DEFAULT == Constants.TIME_FORMAT_DEFAULT) {
            "HH:mm"
        } else {
            "hh:mm a"
        }

        return SimpleDateFormat(format)
    }
}
