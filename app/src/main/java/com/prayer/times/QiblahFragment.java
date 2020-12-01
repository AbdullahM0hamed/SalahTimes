package com.prayer.times;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.internal.QiblaUtil;
import java.util.Date;

public class QiblahFragment extends Fragment implements SensorEventListener
{ 
	private SensorManager sensorManager;
	private Sensor mAccelerometer;
	private Sensor mMagnetometer;
	private View rootView;
	private static final int orientation = SensorManager.SENSOR_ORIENTATION;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.qiblah, container, false);
		rootView = view;
		ImageView qiblahIcon = view.findViewById(R.id.qiblahNavIcon);
		TextView qiblahText = view.findViewById(R.id.qiblahNavText);

		/*
		 * Ideally, this would be part of CommonCode.setupNavivation
		 * but I was unable to get it to work there
		 */
		RelativeLayout salah_times = view.findViewById(R.id.salah_time);
		final Fragment fragment = this;
		salah_times.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					getActivity().getFragmentManager()
				        .beginTransaction()
						.remove(fragment)
						.commit();

					MainActivity activity = (MainActivity) getActivity();
					CommonCode.tintViews(activity.getIcon(), activity.getTextView());
				}
			});

		CommonCode.tintViews(qiblahIcon, qiblahText);
		sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
		mAccelerometer = sensorManager.getDefaultSensor(SensorManager.SENSOR_ORIENTATION);
		mMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sensorManager.registerListener(this, mAccelerometer, orientation);
		MainActivity activity = (MainActivity) getActivity();
		activity.getLocation();
		Coordinates coordinates = new Coordinates(activity.latitude, activity.longitude);
		float qiblahAngle = (float)QiblaUtil.calculateQiblaDirection(coordinates);

		if (mAccelerometer == null || mMagnetometer == null)
		{
			rotateImage(qiblahAngle);
			setSunImage(activity);
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.missing_sensors_title)
				.setMessage(R.string.missing_sensors_description)
				.setCancelable(false)
				.setPositiveButton("I understand", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface p1, int p2)
					{

					}
				});
			builder.create().show();
		}
		else
		{
			float north = getTrueNorthAngle(activity);
			float qiblah = getTrueNorthAngle(activity) + qiblahAngle;
			rotateImage(north, qiblah);
		}

		return view;
	}

	@Override
	public void onSensorChanged(SensorEvent event)
    {

	}

	@Override
	public void onAccuracyChanged(Sensor p1, int p2)
	{

	}

	@Override
	public void onPause()
	{
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		sensorManager.registerListener(this, mAccelerometer, orientation);
	}

	void rotateImage(float angle)
	{
		rotateImage(0, angle);
	}

	void rotateImage(float northAangle, float qiblahAngle)
	{
		ImageView image = rootView.findViewById(R.id.prayer_mat);
		image.setRotation(qiblahAngle);
	
		ImageView dial = rootView.findViewById(R.id.dial_markings);
		dial.setRotation(northAangle);
	}

	void setSunImage(MainActivity activity)
	{
		Date today = new Date();
		PrayerTimes times = CommonCode.getPrayerTimes(activity, today, activity.latitude, activity.longitude);
		int solarImage = (today.getTime() > times.dhuhr.getTime()) ? R.drawable.ic_nesw_sun_west : R.drawable.ic_nesw_sun_east;
		ImageView dialMarkings = rootView.findViewById(R.id.dial_markings);
		dialMarkings.setImageResource(solarImage);
	}

	float getTrueNorthAngle(MainActivity activity)
	{
		float[] R = new float[9];
		float[] values = new float[3];
		SensorManager.getOrientation(R, values);
	
		float azimuth = (float) Math.toDegrees(values[0]);
		GeomagneticField geoField = new GeomagneticField(
		        (float) activity.latitude,
				(float) activity.longitude,
				0,
				(new Date()).getTime());
		
		azimuth += geoField.getDeclination();
		
		return azimuth;
	}
}