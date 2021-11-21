package com.prayer.times.ui.controller.settings

import com.prayer.times.R
import com.prayer.times.ui.controller.base.SettingsController

class GeneralController : SettingsController() {

    override fun getSettingsList() = listOf(
        GeneralItem(
            resources!!.getString(R.string.language_title),
            resources!!.getString(R.string.language_default),
            { android.widget.Toast.makeText(activity as android.content.Context, "Test", 5).show() }
        ),
        GeneralItem(
            resources!!.getString(R.string.calculation_method),
            resources!!.getString(R.string.moonsighting)
        ),
        GeneralItem(
            resources!!.getString(R.string.madhab),
            resources!!.getString(R.string.shafi)
        ),
        GeneralItem(
            resources!!.getString(R.string.time_format),
            resources!!.getString(R.string.military_time)
        )
    )
}
