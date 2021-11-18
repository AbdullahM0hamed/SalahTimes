package com.prayer.times.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.prayer.times.Constants
import com.prayer.times.R
import com.prayer.times.calculation.Calculation
import com.prayer.times.preference.PreferencesHelper
import com.prayer.times.ui.MainActivity
import com.prayer.times.ui.action.UpdateTimes
import com.prayer.times.ui.mainStore
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.PickerType

class Location {

    companion object {
        fun hasPermission(activity: MainActivity): Boolean {
            return (Build.VERSION.SDK_INT < 23 || activity.checkSelfPermission(ACCESS_FINE_LOCATION) != -1)
        }

        fun getLocation(activity: MainActivity) {
            val context = activity as Context
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location == null) {
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, activity as LocationListener)
            } else {
                saveLocation(activity as Context, location?.latitude, location?.longitude)
            }
        }

        fun getManualLocation(activity: MainActivity) {
            val intent = VanillaPlacePicker.Builder(activity as Context)
                    .with(PickerType.MAP_WITH_AUTO_COMPLETE)
                    .setCountry((activity as Context).getResources().getConfiguration().locale.getCountry())
                    .enableShowMapAfterSearchResult(true)
                    .build()

            activity.startActivityForResult(intent, Constants.MAP_RESULT)
        }

        fun saveLocation(context: Context, latitude: Double, longitude: Double) {
            val helper = PreferencesHelper(context)

            helper.putString(Constants.LATITUDE, latitude.toString())
            helper.putString(Constants.LONGITUDE, longitude.toString())

            Toast.makeText(context, R.string.location_updated, 5).show()
            mainStore.dispatch(UpdateTimes())
            Calculation(context).setNextPrayerAlarm()
        }

        fun enableLocationSettings(activity: MainActivity) {
            val locationSettingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivityForResult(locationSettingsIntent, 0)
        }

        fun isEnabled(context: Context): Boolean {
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            return manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        }
    }
}
