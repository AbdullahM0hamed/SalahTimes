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
public class SettingsFragment extends PreferenceFragment {
  @Override
  public void onCreate(Bundle savedInstanceState) {
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
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.preferences_layout, container, false);
    ImageView settingsIcon = view.findViewById(R.id.settingsNavIcon);
    TextView settingsText = view.findViewById(R.id.settingsNavText);

    /*
     * Ideally, this would be part of CommonCode.setupNavivation
     * but I was unable to get it to work there
     */
    RelativeLayout salah_times = view.findViewById(R.id.salah_time);
    final PreferenceFragment fragment = this;
    salah_times.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View view) {
            getActivity().getFragmentManager().beginTransaction().remove(fragment).commit();

            MainActivity activity = (MainActivity) getActivity();
            CommonCode.tintViews(activity.getIcon(), activity.getTextView());
            activity.setPrayerTimes(activity.latitude, activity.longitude);
          }
        });

    CommonCode.tintViews(settingsIcon, settingsText);
    CommonCode.setupNavigation(getActivity(), view);
    return view;
  }
}
