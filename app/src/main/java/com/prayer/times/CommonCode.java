package com.prayer.times;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.Prayer;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import java.util.Arrays;
import java.util.Date;

/*
 * This is a static class which contains
 * constants and methods which are used
 * across multiple classes
 */
class CommonCode
{
	private static final String PREF_SALAH = "PREF_SALAH";
	private static SharedPreferences mPreferences;

	/*
	 * Gets the prayer times for a given day
	 *
	 * @param day The day for which prayer times should be calculated
	 * @param latitude The latitude coordinate of the current location
	 * @param longitude The longitude coordinate of the current location
	 * @return PrayerTimes object which contains a Prayer object for each fard salah in a day and sunrise
	 */
	static PrayerTimes getPrayerTimes(Context context, Date day, double latitude, double longitude)
	{
		Coordinates coordinates = new Coordinates(latitude, longitude);
		DateComponents date = DateComponents.from(day);

		CalculationParameters params = getCalculationParameters(context);
		params.madhab = getMadhab();

		PrayerTimes times = new PrayerTimes(coordinates, date, params);
		return times;
	}

	/*
	 * Returns parameters for the chosen calculation method
	 * in shared preferences. Default is muslim_world_league
	 */
	static CalculationParameters getCalculationParameters(Context context)
	{
		String[] calculationMethods = context.getResources().getStringArray(R.array.calculationMethodValues); 

		String calculationMethod = mPreferences.getString("calculation_method", calculationMethods[0]);
		int position = Arrays.asList(calculationMethods).indexOf(calculationMethod);
		CalculationParameters params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();

		switch (position)
		{
			case 1:
				params = CalculationMethod.EGYPTIAN.getParameters();
				break;
			case 2:
				params = CalculationMethod.KARACHI.getParameters();
				break;
			case 3:
				params = CalculationMethod.UMM_AL_QURA.getParameters();
				break;
			case 4:
				params = CalculationMethod.DUBAI.getParameters();
				break;
			case 5:
				params = CalculationMethod.QATAR.getParameters();
				break;
			case 6:
				params = CalculationMethod.MOON_SIGHTING_COMMITTEE.getParameters();
				break;
			case 7:
				params = CalculationMethod.SINGAPORE.getParameters();
				break;
			case 8:
				params = CalculationMethod.NORTH_AMERICA.getParameters();
				break;
		}

		return params;
	}

	/*
	 * Gets madhab based on shared preferences. Default is shafi
     *
     * @returns Madhab object for a given madhab
	 */
	static Madhab getMadhab()
	{
		String madhab = mPreferences.getString("madhab", "shafi");
		Madhab chosenMadhab = null;

		switch (madhab)
		{
			case "shafi":
				chosenMadhab = Madhab.SHAFI;
				break;
			case "hanafi":
				chosenMadhab = Madhab.HANAFI;
				break;
		}

		return chosenMadhab;
	}

	/*
	 * Returns string format for displaying time in the preferred
	 * way for the user. The default format is 24 hours
	 */
	static String getTimeFormat(Context context)
	{
		mPreferences = context.getSharedPreferences(context.getPackageName() + "_preferences", context.MODE_PRIVATE);
		String chosen = mPreferences.getString("time_format", "24_hour_time");
		String format = "HH:mm";

		switch (chosen)
		{
			case "24_hour_time":
				format = "HH:mm";
				break;
			case "12_hour_time":
				format = "hh:mm a";
				break;
		}

		return format;
	}

	/*
     * This method is responsible for setting alarms
     * for prayer times
     *
     * @param times a list of prayer times for which reminders should be set
     */
	static void setReminders(Context context, PrayerTimes times)
	{
		Intent intent;
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		Date[] prayerDates = new Date[] {times.fajr, times.dhuhr, times.asr, times.maghrib, times.isha};

		Prayer[] prayers = new Prayer[5];
		for (int i = 0; i < 5; i++) prayers[i] = times.currentPrayer(prayerDates[i]);

		for (Prayer salah : prayers)
		{
			intent = new Intent(context, AlarmReceiver.class);
			intent.putExtra("SALAH_NAME", salah.name());
			intent.setAction("intent.action.SET_PRAYER_REMINDER");
			PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
			alarmManager.set(AlarmManager.RTC_WAKEUP, times.timeForPrayer(salah).getTime(), alarmIntent);
		}
	}
}