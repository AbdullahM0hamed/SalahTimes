package com.prayer.times.ui.controller.settings

import com.prayer.times.R
import com.prayer.times.ui.controller.base.SettingsController

class SettingsControllerMain : SettingsController() {

    override fun getSettingsList() {
        val size = 16 * resources!!.getDisplayMetrics().scaledDensity
        
        val settingsList = listOf(
            SettingsItemMain(
                resources!!.getString(R.string.settings_general),
                R.drawable.ic_general_24dp,
                size,
                this
            ),
            SettingsItemMain(
                resources!!.getString(R.string.settings_themes),
                R.drawable.ic_theme_24dp,
                size,
                this
            ),
            SettingsItemMain(
                resources!!.getString(R.string.settings_relocate),
                R.drawable.ic_relocate_24dp,
                size,
                this
            ),
            SettingsItemMain(
                resources!!.getString(R.string.settings_about),
                R.drawable.ic_about_24dp,
                size,
                this
            )
        )

        return settingsList
    }
}
