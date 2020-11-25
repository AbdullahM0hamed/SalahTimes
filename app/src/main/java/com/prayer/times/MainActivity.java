package com.prayer.times;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.Prayer;
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements LocationListener
{
	private Location mLocation;
	private Calendar mCurrentCal = Calendar.getInstance(Locale.getDefault());
	private TextView mHijriDateView;
	private TextView mDateView;
	private long mLatitude; 
	private long mLongitude;
	private SharedPreferences mPreferences;
	private final static String LATITUDE = "PREF_LATITUDE";
	private final static String LONGITUDE = "PREF_LONGITUDE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		OnClickListener listenerLeft = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				addToDate(-1);
			}
		};

		OnClickListener listenerRight = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				addToDate(1);
			}
		};

		setContentView(R.layout.main);

		ImageButton subtractDate = findViewById(R.id.dayBefore);
		ImageButton addDate = findViewById(R.id.dayAfter);

		subtractDate.setOnClickListener(listenerLeft);
		addDate.setOnClickListener(listenerRight);

		mHijriDateView = findViewById(R.id.hijriDate);
		mHijriDateView.setText(getHijriDate());
		mDateView = findViewById(R.id.date);
		mDateView.setText(getDate());
	
		mPreferences = this.getSharedPreferences(this.getPackageName() + "_preferences", this.MODE_PRIVATE);
        mLongitude = mPreferences.getLong(LONGITUDE, 360);
        mLatitude = mPreferences.getLong(LATITUDE, 360);

		setupNavigation();       

        //Valid latitudes are between -90 and 90, and valid longitudes are between -180 and 180
        if (mLongitude == 360 || mLatitude == 360) 
        {
			checkPermissionAndGetLocation();
		}
		else
		{
			setPrayerTimes(mLatitude, mLongitude);
		}
    }

	/*
	 * startActivityForResult is only used to redirect user to
	 * location settings, therefore this method attempts to get
	 * location
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		getLocation();
	}

	/*
	 * Only location permissions are requested, so if permission
	 * is granted, then we get location - otherwise we ask again
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		boolean denied = false;

		if (grantResults[0] == PackageManager.PERMISSION_DENIED) denied = true;

		if (denied)
		{
			checkPermissionAndGetLocation();
		}
		else
		{
			getLocation();
		}
	}

	/*
	 * Stores new coordinates when location data is received
	 * in SharedPreferences and sets prayer times based on
	 * the new coordinates. Ideally, this could be registered
	 * and unregistered in onResume and onPause respectively
	 * so the app wouldn't always be listening for location
	 * info
	 */
	@Override
	public void onLocationChanged(Location p1)
	{
		setLocation(p1);
		mLatitude = (long) p1.getLatitude();
		mLongitude = (long) p1.getLongitude();

		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putLong(LATITUDE, mLatitude);
		editor.putLong(LONGITUDE, mLongitude);
		editor.apply();

		setPrayerTimes(mLatitude, mLongitude);
	}

	@Override
	public void onStatusChanged(String p1, int p2, Bundle p3) {}

	@Override
	public void onProviderEnabled(String p1) {}

	@Override
	public void onProviderDisabled(String p1) {}

	void setLocation(Location loc)
	{
		this.mLocation = loc;
	}

	/*
	 * Sets the onclick listeners on the bottom nav bar
	 * as well as calling change_screen with the parameter
	 * 'salah_timings', which results in the initial setting
	 * of prayer timings
	 */
	void setupNavigation()
	{
		changeScreen("salah_timings");

		OnClickListener navigateToTimingsListener = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				changeScreen("salah_timings");
			}	
		};

		OnClickListener navigateToQiblahListener = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				changeScreen("qiblah_compass");
			}	
		};

		OnClickListener navigateToSettingsListener = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				changeScreen("settings");
			}	
		};

		RelativeLayout salah_times = findViewById(R.id.salah_time);
		salah_times.setOnClickListener(navigateToTimingsListener);

		RelativeLayout qiblah = findViewById(R.id.qiblah_compass);
		qiblah.setOnClickListener(navigateToQiblahListener);

		RelativeLayout settings = findViewById(R.id.settings);
		settings.setOnClickListener(navigateToSettingsListener);
	}

	/*
	 * Redirects user to location settings
	 */
	void enableLocation()
	{
		Intent enableLocationIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivityForResult(enableLocationIntent, 0);
	}

	/*
	 * This method is responsible for changing
	 * between screens when user clicks 
	 * a button on the bottom nav bar
	 */
	void changeScreen(String screen)
	{
		ImageView timingIcon = findViewById(R.id.timingNavIcon);
		ImageView qiblahIcon = findViewById(R.id.qiblahNavIcon);

		timingIcon.clearColorFilter();
		qiblahIcon.clearColorFilter();

		TextView timingText = findViewById(R.id.timingNavText);
		TextView qiblahText = findViewById(R.id.qiblahNavText);

		int defaultColor = Color.parseColor("#757575");

		timingText.setTextColor(defaultColor);
		qiblahText.setTextColor(defaultColor);

		int selectedColor = Color.parseColor("#2d3e50");

		switch (screen)
		{
			case "salah_timings":
				timingIcon.setColorFilter(selectedColor);
				timingText.setTextColor(selectedColor);
				if (mLatitude != 360 && mLongitude != 360) setPrayerTimes(mLatitude, mLongitude);
				break;
			case "qiblah_compass":
				qiblahIcon.setColorFilter(selectedColor);
				qiblahText.setTextColor(selectedColor);
				break;
			case "settings":
				getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
				break;
		}
	}

	/*
	 * Method for changing the Hijri and Gregorian dates
	 * displayed in the app.
	 */
	void addToDate(int days)
	{
		mCurrentCal.add(Calendar.DATE, days);
		setPrayerTimes(mLatitude, mLongitude);
		mHijriDateView.setText(getHijriDate());
		mDateView.setText(getDate());
	}

	/*
	 * Get currently selected Gregorian date as Day, day_of_month month year
	 * e.g: Tuesday, 24th November 2020
	 */
	String getDate()
	{
		SimpleDateFormat sFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");

		return sFormat.format(mCurrentCal.getTime());
	}

	/*
	 * Get currently selected Hijri date, formatted in the
	 * same way as getDate()
	 */
	String getHijriDate()
	{
		UmmalquraCalendar calendar = new UmmalquraCalendar();

		calendar.get(Calendar.YEAR);
		calendar.get(Calendar.MONTH);
		calendar.get(Calendar.DAY_OF_MONTH);

		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");
		formatter.setCalendar(calendar);

		return formatter.format(mCurrentCal.getTime());
	}

	/*
	 * If location permissions are available, location is requested,
	 * otherwise location permissions are requested
	 */
	void checkPermissionAndGetLocation()
	{
		String permission = "android.permission.ACCESS_FINE_LOCATION";

		if (Build.VERSION.SDK_INT < 23 || checkSelfPermission(permission) != -1) getLocation();

		requestPermissions(new String[] {permission}, 123);
	}

	/*
	 * This method is used to get location. When a cached location
	 * is available, as provided by LocationManager.getLastKnownLocation
	 * then that is passed on to the onLocationChanged method,
	 * otherwise locationUpdates are requested - which trigger
	 * onLocationChanged
	 */
	void getLocation()
	{
		LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

		boolean isEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isEnabled)
		{
			enableLocation();
		}
		else
		{
			mLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (mLocation == null)
			{
				manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			}
			else
			{
				onLocationChanged(mLocation);
			}
		}
	}

	/*
	 * This method loops through the TextViews that display prayer 
	 * times, and sets the prayer times based on the received
	 * latitude and longitude parameters - as well as the stored
	 * calculation method and madhab in shared preferences.
	 * The default calculation method is MUSLIM_WORLD_LEAGUE, and
	 * the default madhab is Shafi'i
	 */
	void setPrayerTimes(double latitude, double longitude)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(CommonCode.getTimeFormat(this), Locale.US);
		formatter.setTimeZone(TimeZone.getDefault());
	
		PrayerTimes times = CommonCode.getPrayerTimes(this, mCurrentCal.getTime(), latitude, longitude);
		int[] ids = new int[] {R.id.fajr_start, R.id.sunrise_start, R.id.dhuhr_start, R.id.asr_start, R.id.maghrib_start, R.id.isha_start};
		Date[] timeList = new Date[] {times.fajr, times.sunrise, times.dhuhr, times.asr, times.maghrib, times.isha};

		for (int i = 0; i < timeList.length; i++)
		{
			TextView textView = findViewById(ids[i]);
			textView.setText(formatter.format(timeList[i]));
		}

		times = CommonCode.getPrayerTimes(this, new Date(), latitude, longitude);
		Prayer nextPrayer = times.nextPrayer();
		TextView next = findViewById(R.id.nextPrayer);

		//If 'Isha has already come, getting the next Prayer's time throws a NullPointerException
		try 
		{
			Date nextPrayerTime = times.timeForPrayer(nextPrayer);
		
			//Gets the prayer following sunrise, thus effectively skipping sunrise as next prayer
			if (nextPrayer.name() == "SUNRISE") 
			{
				nextPrayerTime = times.timeForPrayer(times.nextPrayer(times.sunrise));
			}
			next.setText(getResources().getString(R.string.next_prayer, formatter.format(nextPrayerTime)));
		}
		catch (NullPointerException e)
		{
			Calendar calendar = Calendar.getInstance(Locale.getDefault());
			calendar.add(Calendar.DATE, 1);
			Date tomorrow = calendar.getTime();
			PrayerTimes tomorrowTimes = CommonCode.getPrayerTimes(this, tomorrow, latitude, longitude);
			next.setText(getResources().getString(R.string.next_prayer, formatter.format(tomorrowTimes.fajr)));
			timeList = new Date[] {times.fajr, times.sunrise, times.dhuhr, times.asr, times.maghrib, times.isha};
		}
	}
}