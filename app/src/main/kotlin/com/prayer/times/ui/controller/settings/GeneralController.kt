package com.prayer.times.ui.controller.settings

import android.content.Context
import com.prayer.times.Constants
import com.prayer.times.R
import com.prayer.times.ui.controller.base.SettingsController

class GeneralController : SettingsController() {

    override fun getHeaderTitle() = resources!!.getString(R.string.settings_general)
    override fun getSettingsList(): List<GeneralItem> {
        val context = activity as Context
    
        return listOf(
            GeneralItem(
                context,
                resources!!.getString(R.string.language_title),
                resources!!.getString(R.string.language_summary),
                Constants.LANGUAGE_KEY,
                Constants.LANGUAGE_DEFAULT,
                resources!!.getStringArray(R.array.languages),
                resources!!.getStringArray(R.array.language_values)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.calculation_method),
                resources!!.getString(R.string.calculation_summary),
                Constants.CALCULATION_KEY,
                Constants.CALCULATION_DEFAULT,
                resources!!.getStringArray(R.array.calculation_methods),
                resources!!.getStringArray(R.array.calculation_method_values)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.madhab),
                resources!!.getString(R.string.madhab_summary),
                Constants.NADHAB_KEY,
                Constants.MADHAB_DEFAULT,
                resources!!.getStringArray(R.array.madaahib),
                resources!!.getStringArray(R.array.madaahib_values)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.time_format),
                resources!!.getString(R.string.time_format_summary),
                Constants.TIME_FORMAT_KEY,
                Constants.TIME_FORMAT_DEFAULT,
                resources!!.getStringArray(R.array.time_formats),
                resources!!.getStringArray(R.array.time_format_values)
            )
        )
    }
}
