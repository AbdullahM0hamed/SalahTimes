package com.prayer.times.ui.controller.settings

import android.content.Context
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
                "language",
                "english",
                resources!!.getStringArray(R.array.languages),
                resources!!.getStringArray(R.array.language_values)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.calculation_method),
                resources!!.getString(R.string.calculation_summary),
                "calculation_method",
                "moonsighting",
                resources!!.getStringArray(R.array.calculation_methods),
                resources!!.getStringArray(R.array.calculation_method_values)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.madhab),
                resources!!.getString(R.string.madhab_summary),
                "madhab",
                "shafi",
                resources!!.getStringArray(R.array.madaahib),
                resources!!.getStringArray(R.array.madaahib_values)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.time_format),
                resources!!.getString(R.string.time_format_summary),
                "time_format",
                "24_hour_time",
                resources!!.getStringArray(R.array.time_formats),
                resources!!.getStringArray(R.array.time_format_values)
            )
        )
    }
}
