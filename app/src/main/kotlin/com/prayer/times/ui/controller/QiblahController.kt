package com.prayer.times.ui.controller

import android.app.Activity
import android.content.Context
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prayer.times.Constants
import com.prayer.times.R
import com.prayer.times.calculation.Calculation
import com.prayer.times.databinding.QiblahBinding
import com.prayer.times.preference.PreferencesHelper
import com.prayer.times.ui.MainActivity
import com.prayer.times.ui.action.SetRotation
import com.prayer.times.ui.action.UpdateSunState
import com.prayer.times.ui.controller.base.BaseController
import com.prayer.times.ui.mainStore
import com.prayer.times.ui.state.CompassState
import org.reduxkotlin.StoreSubscription
import java.util.Date

class QiblahController :
    BaseController<QiblahBinding>(),
    SensorEventListener {

    private lateinit var storeSubscription: StoreSubscription
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var gravity = FloatArray(3)
    private var smoothed = FloatArray(3)
    private var geomagnetic = FloatArray(3)
    private var rotation = FloatArray(9)
    private var orientation = FloatArray(3)
    private lateinit var helper: PreferencesHelper
    private lateinit var  calculator: Calculation

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View {
        binding = QiblahBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        storeSubscription = mainStore.subscribe { newState(mainStore.state.compassState) }

        if ((activity as MainActivity).isLocationStored()) {
            sensorManager = (activity as Context).getSystemService(SensorManager::class.java)
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            helper = PreferencesHelper(activity as Context)
            calculator = Calculation(activity as Context)

            if (accelerometer == null || magnetometer == null) {
                val times = calculator.getPrayerTimes(Date())

                if (Date().getTime() > times.dhuhr.getTime()) {
                    mainStore.dispatch(UpdateSunState(Constants.SUN_WEST))
                } else {
                    mainStore.dispatch(UpdateSunState(Constants.SUN_EAST))
                }

                if (mainStore.state.compassState.qiblahAngle != calculator.getQiblahDirection()) {
                    mainStore.dispatch(SetRotation(0f, calculator.getQiblahDirection()))
                }
            } else {
                registerListener()
            }
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        registerListener()
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        unregisterListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterListener()
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        registerListener()
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        unregisterListener()
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.getType()) {
            Sensor.TYPE_ACCELEROMETER -> {
                smoothed = lowPassFilter(event.values, gravity)
                gravity[0] = smoothed[0]
                gravity[1] = smoothed[1]
                gravity[2] = smoothed[2]
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                smoothed = lowPassFilter(event.values, geomagnetic)
                geomagnetic[0] = smoothed[0]
                geomagnetic[1] = smoothed[1]
                geomagnetic[2] = smoothed[2]
            }
        }

        SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic)
        SensorManager.getOrientation(rotation, orientation)
        var bearing = Math.toDegrees(orientation[0].toDouble()).toFloat()

        val latitude = helper.getString(Constants.LATITUDE)!!.toFloat()
        val longitude = helper.getString(Constants.LONGITUDE)!!.toFloat()
        val geoMagneticField = GeomagneticField(
            latitude,
            longitude,
            0f,
            System.currentTimeMillis()
        )

        bearing += geoMagneticField.getDeclination()

        if (bearing < 0) {
            bearing += 360
        }

        mainStore.dispatch(SetRotation(360 - bearing, (360 - bearing) + calculator.getQiblahDirection()))
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    fun registerListener() {
        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun unregisterListener() {
        if (accelerometer != null && magnetometer != null) {
            sensorManager.unregisterListener(this, accelerometer)
            sensorManager.unregisterListener(this, magnetometer)
        }
    }

    fun lowPassFilter(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) return input

        return input.mapIndexed { index, value ->
            output[index] + 0.25f * (value - output[index])
        }.toList().toFloatArray()
    }

    fun newState(state: CompassState) {
        if (state.sunState != Constants.SUN_NONE) {
            val sunImage = when (state.sunState) {
                Constants.SUN_EAST -> R.drawable.ic_nesw_sun_east
                Constants.SUN_WEST -> R.drawable.ic_nesw_sun_west
                else -> R.drawable.ic_nesw
            }

            binding.dialMarkings.setImageResource(sunImage)
        }

        binding.prayerMat.rotation = state.qiblahAngle
        binding.dialMarkings.rotation = state.northAngle
    }
}
