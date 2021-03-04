package com.prayer.times.ui

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.location.LocationListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.prayer.times.Constants
import com.prayer.times.R
import com.prayer.times.calculation.Calculation
import com.prayer.times.databinding.MainActivityBinding
import com.prayer.times.location.Location
import com.prayer.times.preference.PreferencesHelper
import com.prayer.times.receiver.AlarmReceiver
import com.prayer.times.ui.action.UpdateTimes
import com.prayer.times.ui.controller.QiblahController
import com.prayer.times.ui.controller.TimesController
import com.prayer.times.ui.controller.settings.SettingsControllerMain
import com.prayer.times.ui.controller.base.BaseController
import com.prayer.times.ui.reducer.appStateReducer
import com.prayer.times.ui.state.AppState
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import java.util.Date
import org.reduxkotlin.createThreadSafeStore

val mainStore = createThreadSafeStore(::appStateReducer, AppState())

class MainActivity :
    AppCompatActivity(),
    LocationListener,
    PermissionListener {

    private lateinit var binding: MainActivityBinding
    private lateinit var router: Router

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        if (!isTaskRoot) {
            finish()
            return
        }


        if (!isLocationStored()) {
            if (!Location.hasPermission(this)) {
                Dexter.withContext(this)
                    .withPermission(ACCESS_FINE_LOCATION)
                    .withListener(this)
                    .check()
            } else {
                Location.getLocation(this)
            }
        } else {
            Calculation(this).setNextPrayerAlarm()
        }

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        router = Conductor.attachRouter(this, binding.controllerContainer, savedInstance)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId

            val currentRoot = router.backstack.firstOrNull()
            if (currentRoot?.tag()?.toIntOrNull() != id) {
                when (id) {
                    R.id.nav_times -> setRoot(TimesController())
                    R.id.nav_qiblah -> setRoot(QiblahController())
                    R.id.nav_settings -> setRoot(SettingsControllerMain())
                }
            }
            true
        }

        if (!router.hasRootController()) {
            binding.bottomNavigation.selectedItemId = R.id.nav_times
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.MAP_RESULT -> {
                if (data != null) {
                    val address = VanillaPlacePicker.onActivityResult(data)
                    val latitude: Double = address?.latitude ?: 0.0
                    val longitude: Double = address?.longitude ?: 0.0
                    Location.saveLocation(this, latitude, longitude)
                }
            }
            0 -> {
                if (Location.hasPermission(this)) {
                    Location.getLocation(this)
                } else {
                    Location.getManualLocation(this)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse) {
        if (response.getPermissionName() == ACCESS_FINE_LOCATION) {
            if (!Location.isEnabled(this)) {
                Location.enableLocationSettings(this)
            } else {
                Location.getLocation(this)
            }
        }
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse) {
        if (response.getPermissionName() == ACCESS_FINE_LOCATION) {
            Location.getManualLocation(this)
        }
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) = token.continuePermissionRequest()

    override fun onLocationChanged(location: android.location.Location) = Location.saveLocation(this, location.latitude, location.longitude)
    override fun onStatusChanged(provider: String?, status: Int, extra: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

    fun setRoot(controller: BaseController<*>) {
        router.setRoot(RouterTransaction.with(controller))
    }

    fun isLocationStored(): Boolean {
        val helpers = PreferencesHelper(this)
        return !(helpers.getString(Constants.LATITUDE)?.isNullOrEmpty() ?: true || helpers.getString(Constants.LONGITUDE)?.isNullOrEmpty() ?: true)
    }
}
