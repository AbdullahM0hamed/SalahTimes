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
                "language"
                resources!!.getString(R.string.language_default),
                resources!!.getStringArray(R.array.languages)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.calculation_method),
                resources!!.getString(R.string.calculation_summary),
                "calculation_method"
                resources!!.getString(R.string.moonsighting),
                resources!!.getStringArray(R.array.madaahib)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.madhab),
                resources!!.getString(R.string.madhab_summary),
                "madhab",
                resources!!.getString(R.string.shafi),
                resources!!.getStringArray(R.array.madaahib)
            ),
            GeneralItem(
                context,
                resources!!.getString(R.string.time_format),
                resources!!.getString(R.string.time_format_summary),
                "time_format",
                resources!!.getString(R.string.military_time),
                resources!!.getStringArray(R.array.timeFormats)
            )
        )
    }
}
