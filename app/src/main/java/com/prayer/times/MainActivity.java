package com.prayer.times;

import android.app.Activity;
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
import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.Madhab;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.Prayer;
import com.batoulapps.adhan.data.DateComponents;
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import android.widget.*;

public class MainActivity extends AppCompatActivity implements LocationListener
{
	private Location location;
	private Calendar currentCal = Calendar.getInstance(Locale.getDefault());
	private TextView hijriDateView;
	private TextView dateView;
	private long latitude = 360;
	private long longitude = 360;

	void setLocation(Location loc)
	{
		this.location = loc;
	}

	@Override
	public void onLocationChanged(Location p1)
	{
		setLocation(p1);
		latitude = (long) p1.getLatitude();
		longitude = (long) p1.getLongitude();

		SharedPreferences.Editor editor = getSharedPreferences("location", 0).edit();
		editor.putLong("latitude", latitude);
		editor.putLong("longitude", longitude);
		editor.apply();

		setPrayerTimes(latitude, longitude);
	}

	@Override
	public void onStatusChanged(String p1, int p2, Bundle p3)
	{
		// TODO: Implement this method
	}

	@Override
	public void onProviderEnabled(String p1)
	{
		// TODO: Implement this method
	}

	@Override
	public void onProviderDisabled(String p1)
	{
		// TODO: Implement this method
	}

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

		hijriDateView = findViewById(R.id.hijriDate);
		hijriDateView.setText(getHijriDate());

		dateView = findViewById(R.id.date);
		dateView.setText(getDate());

		try
		{
			setupNavigation();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.toString(), 5).show();
		}

        SharedPreferences prefs = this.getSharedPreferences("location", 0);
        longitude = prefs.getLong("longitude", 360);
        latitude = prefs.getLong("latitude", 360);

        //Valid latitudes are between -90 and 90, and valid longitudes are between -180 and 180
        if (longitude == 360 || latitude == 360) 
        {
			checkPermissionAndGetLocation();
		}
		else
		{
			setPrayerTimes(latitude, longitude);
		}
    }

	void enableLocation()
	{
		Intent enableLocationIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivityForResult(enableLocationIntent, 0);
	}

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
				if (latitude != 360 && longitude != 360)
					setPrayerTimes(latitude, longitude);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		try
		{
			getLocation();
		}
		catch (Exception e)
		{
		}
	}

	void addToDate(int days)
	{
		currentCal.add(Calendar.DATE, days);
		setPrayerTimes(latitude, longitude);
		hijriDateView.setText(getHijriDate());
		dateView.setText(getDate());
	}

	String getDate()
	{
		SimpleDateFormat sFormat = new SimpleDateFormat();
		sFormat.applyPattern("EEEE, dd MMMM yyyy");

		return sFormat.format(currentCal.getTime());
	}

	String getHijriDate()
	{
		UmmalquraCalendar calendar = new UmmalquraCalendar();

		calendar.get(Calendar.YEAR);
		calendar.get(Calendar.MONTH);
		calendar.get(Calendar.DAY_OF_MONTH);

		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");
		formatter.setCalendar(calendar);

		return formatter.format(currentCal.getTime());
	}

	void checkPermissionAndGetLocation()
	{
		String permission = "android.permission.ACCESS_FINE_LOCATION";

		if (Build.VERSION.SDK_INT < 23 || checkSelfPermission(permission) != -1)
			getLocation();

		requestPermissions(new String[] {permission}, 123);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		boolean denied = false;

		for (int result : grantResults)
		{
			if (result == PackageManager.PERMISSION_DENIED)
			{
				denied = true;
			}
		}

		if (denied)
		{
			checkPermissionAndGetLocation();
		}
		else
		{
			getLocation();
		}
	}

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
			location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location == null)
			{
				manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
			}
			else
			{
				onLocationChanged(location);
			}
		}
	}

	void setPrayerTimes(double latitude, double longitude)
	{
		Coordinates coordinates = new Coordinates(latitude, longitude);
		DateComponents date = DateComponents.from(currentCal.getTime());
		CalculationParameters params = getCalculationParameters();
		params.madhab = getMadhab();

		PrayerTimes times = new PrayerTimes(coordinates, date, params);

		SimpleDateFormat formatter = new SimpleDateFormat(getTimeFormat(), Locale.US);
		formatter.setTimeZone(TimeZone.getDefault());

		int[] ids = new int[] {R.id.fajr_start, R.id.sunrise_start, R.id.dhuhr_start, R.id.asr_start, R.id.maghrib_start, R.id.isha_start};
		Date[] timeList = new Date[] {times.fajr, times.sunrise, times.dhuhr, times.asr, times.maghrib, times.isha};

		for (int i = 0; i < timeList.length; i++)
		{
			TextView t = findViewById(ids[i]);
			t.setText(formatter.format(timeList[i]));
		}

		times = new PrayerTimes(coordinates, DateComponents.from(new Date()), params);
		Prayer nextPrayer = times.nextPrayer();

		TextView next = findViewById(R.id.nextPrayer);

		//If 'Isha has already come, getting the next Prayer's time throws a NullPointerException
		try 
		{
			Date nextPrayerTime = times.timeForPrayer(nextPrayer);
			next.setText("Next Prayer: " + formatter.format(nextPrayerTime));

		}
		catch (NullPointerException e)
		{
			Calendar calendar = Calendar.getInstance(Locale.getDefault());
			calendar.add(Calendar.DATE, 1);
			Date tomorrow = calendar.getTime();
			PrayerTimes tomorrowTimes = new PrayerTimes(coordinates, DateComponents.from(tomorrow), params);
			next.setText("Next Prayer: " + formatter.format(tomorrowTimes.fajr));
		}

	}

	CalculationParameters getCalculationParameters()
	{
		SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		String[] calculationMethods = getResources().getStringArray(R.array.calculationMethodValues);

		String calculationMethod = prefs.getString("calculation_method", calculationMethods[0]);
		int position = Arrays.asList(calculationMethods).indexOf(calculationMethod);
		CalculationParameters params = null;

		switch (position)
		{
			default:
				params = CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
				break;
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

	Madhab getMadhab()
	{
		SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		String madhab = prefs.getString("madhab", "shafi");
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

	String getTimeFormat()
	{
		SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
		String chosen = prefs.getString("time_format", "24_hour_time");
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
}