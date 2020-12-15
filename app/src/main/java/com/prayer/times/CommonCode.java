package com.prayer.times;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.Prayer;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import android.widget.*;

/*
 * This is a static class which contains
 * constants and methods which are used
 * across multiple classes
 */
class CommonCode {
  private static SharedPreferences preferences;
  private static QiblahFragment qiblahFragment = new QiblahFragment();
  private static SettingsFragment settingsFragment = new SettingsFragment();
  private static HashMap salahStringMap =
      new HashMap<String, Integer>() {
        {
          put("FAJR", R.string.fajr);
          put("DHUHR", R.string.dhuhr);
          put("ASR", R.string.asr);
          put("MAGHRIB", R.string.maghrib);
          put("ISHA", R.string.isha);
        }
      };
  public static final String PREF_SALAH = "PREF_SALAH";
  public static final String PREF_LATITUDE = "PREF_LATITUDE";
  public static final String PREF_LONGITUDE = "PREF_LONGITUDE";

  public static class Salah {
    private String name;
    private Date time;

    public void setName(String newName) {
      this.name = newName;
    }

    public String getName() {
      return this.name;
    }

    public void setDate(Date newTime) {
      this.time = newTime;
    }

    public Date getTime() {
      return this.time;
    }
  }

  /*
   * Gets the prayer times for a given day
   *
   * @param day The day for which prayer times should be calculated
   * @param latitude The latitude coordinate of the current location
   * @param longitude The longitude coordinate of the current location
   * @return PrayerTimes object which contains a Prayer object for each fard salah in a day and sunrise
   */
  static PrayerTimes getPrayerTimes(Context context, Date day, double latitude, double longitude) {
    Coordinates coordinates = new Coordinates(latitude, longitude);
    DateComponents date = DateComponents.from(day);

    CalculationParameters params = getCalculationParameters(context);
    params.madhab = getMadhab();

    PrayerTimes times = new PrayerTimes(coordinates, date, params);
    return times;
  }

  static Salah getNextPrayer(Context context) {
    Calendar calendar = Calendar.getInstance();
    double latitude = Double.valueOf(preferences.getString(PREF_LATITUDE, ""));
    double longitude = Double.valueOf(preferences.getString(PREF_LONGITUDE, ""));
    PrayerTimes times = getPrayerTimes(context, calendar.getTime(), latitude, longitude);

    Prayer nextPrayer = null;
    nextPrayer = times.nextPrayer();

    if (nextPrayer.name() == "NONE") {
      calendar.add(Calendar.DATE, 1);
      times = getPrayerTimes(context, calendar.getTime(), latitude, longitude);
      nextPrayer = times.currentPrayer(times.fajr);
    }

    Salah nextSalah = new Salah();

    if (nextPrayer.name() == "SUNRISE") {
      nextPrayer = times.nextPrayer(times.sunrise);
    }

    String name = nextPrayer.name();
    nextSalah.setName(context.getResources().getString((int) salahStringMap.get(name)));
    nextSalah.setDate(times.timeForPrayer(nextPrayer));
    return nextSalah;
  }

  /*
   * @return the parameters for the chosen calculation method
   * in shared preferences. Default is muslim_world_league
   */
  static CalculationParameters getCalculationParameters(Context context) {
    String[] calculationMethods =
        context.getResources().getStringArray(R.array.calculationMethodValues);

    String calculationMethod = preferences.getString("calculation_method", calculationMethods[0]);
    int position = Arrays.asList(calculationMethods).indexOf(calculationMethod);
    CalculationParameters params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();

    switch (position) {
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
   * @return Madhab object for a given madhab
   */
  static Madhab getMadhab() {
    String madhab = preferences.getString("madhab", "shafi");
    Madhab chosenMadhab = null;

    switch (madhab) {
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
   * @return string format for displaying time in the preferred
   * way for the user. The default format is 24 hours
   */
  static String getTimeFormat(Context context) {
    preferences =
        context.getSharedPreferences(
            context.getPackageName() + "_preferences", context.MODE_PRIVATE);
    String chosen = preferences.getString("time_format", "24_hour_time");
    String format = "HH:mm";

    switch (chosen) {
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
   * @param context the content which will be used to get the alarm service
   * @param name the name of the salah for which the reminder is to be set
   * @param time the time of the salah for which the reminder is to be set
   */
  static void setReminder(Context context, String name, Date time) {
    Intent intent;
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

    intent = new Intent(context, AlarmReceiver.class);
    intent.putExtra(PREF_SALAH, name);
    intent.setAction("intent.action.SET_PRAYER_REMINDER");
    PendingIntent alarmIntent =
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    Intent infoIntent = new Intent(context, MainActivity.class);
    infoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    PendingIntent pInfoIntent = PendingIntent.getActivity(context, 0, infoIntent, 0);

    alarmManager.setAlarmClock(
        new AlarmManager.AlarmClockInfo(time.getTime(), pInfoIntent), alarmIntent);
  }

  /*
   * This method is responsible for changing
   * between screens when user clicks
   * a button on the bottom nav bar
   *
   * @param context The context to be used to get the fragmentManager
   * @param rootView The root view to be used to get the sub-views in the bottom navigation bar
   * @param screen The name of the screen to navigate to
   */
  static void changeScreen(Context context, View rootView, String screen) {
    ImageView timingIcon = rootView.findViewById(R.id.timingNavIcon);
    ImageView qiblahIcon = rootView.findViewById(R.id.qiblahNavIcon);

    timingIcon.clearColorFilter();
    qiblahIcon.clearColorFilter();

    TextView timingText = rootView.findViewById(R.id.timingNavText);
    TextView qiblahText = rootView.findViewById(R.id.qiblahNavText);

    int defaultColor = Color.parseColor("#757575");

    timingText.setTextColor(defaultColor);
    qiblahText.setTextColor(defaultColor);

    Activity activity = (Activity) context;

    switch (screen) {
      case "qiblah_compass":
        activity
            .getFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, qiblahFragment)
            .commit();
        break;
      case "settings":
        activity
            .getFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, settingsFragment)
            .commit();
        break;
    }
  }

  /*
   * Sets the onclick listeners on the bottom nav bar
   * as well as calling change_screen with the parameter
   * 'salah_timings', which results in the initial setting
   * of prayer timings
   *
   * @param context The context to be passed to the change_screen method
   * @param rootView The root view from which we can extract the sub-views in the bottom navigation bar
   */
  static void setupNavigation(final Context context, final View rootView) {
    OnClickListener navigateToQiblahListener =
        new OnClickListener() {
          @Override
          public void onClick(View view) {
            changeScreen(context, rootView, "qiblah_compass");
          }
        };

    OnClickListener navigateToSettingsListener =
        new OnClickListener() {
          @Override
          public void onClick(View view) {
            changeScreen(context, rootView, "settings");
          }
        };

    RelativeLayout qiblah = rootView.findViewById(R.id.qiblah_compass);
    qiblah.setOnClickListener(navigateToQiblahListener);

    RelativeLayout settings = rootView.findViewById(R.id.settings);
    settings.setOnClickListener(navigateToSettingsListener);
  }

  /*
   * This method is used tint the views
   * that should appear selected in the
   * bottom navigation bar
   *
   * @param imageView The ImageView containing the icon
   * @param textView The textVirw containing the name of the screen
   */
  static void tintViews(ImageView imageView, TextView textView) {
    int selectedColor = Color.parseColor("#2d3e50");
    imageView.setColorFilter(selectedColor);
    textView.setTextColor(selectedColor);
  }
}
