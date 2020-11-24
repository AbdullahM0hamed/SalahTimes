package com.prayer.times;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/*
 * This class allows the user to configure
 * preferences, such as calculation method
 * etc. This class does not contain code for
 * getting or setting shared preferencses
 * values, that is done via PreferenceScreen
 * See: res/xml/preferences.xml
 */
public class SettingsFragment extends PreferenceFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	/*
	 * This method is overriden in order
	 * to allow us to set onClickListeners
	 * on a custom layout, to allow for
	 * bottom bar navigation.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.preferences_layout, container, false);
		ImageView settingsIcon = view.findViewById(R.id.settingsNavIcon);
		TextView settingsText = view.findViewById(R.id.settingsNavText);
	
		int color = Color.parseColor("#2d3e50");
		settingsIcon.setColorFilter(color);
		settingsText.setTextColor(color);
		
		OnClickListener navigateToTimingsListener = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				changeScreen("salah_timings", view);
			}	
		};

		OnClickListener navigateToQiblahListener = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				changeScreen("qiblah_compass", view);
			}	
		};

		OnClickListener navigateToSettingsListener = new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				changeScreen("settings", view);
			}	
		};
	
		RelativeLayout salah_times = view.findViewById(R.id.salah_time);
		salah_times.setOnClickListener(navigateToTimingsListener);

		RelativeLayout qiblah = view.findViewById(R.id.qiblah_compass);
		qiblah.setOnClickListener(navigateToQiblahListener);

		RelativeLayout settings = view.findViewById(R.id.settings);
		settings.setOnClickListener(navigateToSettingsListener);
	
		return view;
	}

	void changeScreen(String screen, View view)
	{

		switch (screen)
		{
			default:
				((MainActivity) getActivity()).changeScreen(screen);
				getActivity().getFragmentManager().beginTransaction().remove(this).commit();
				break;
			case "settings":
				break;
		}
	}
	
}