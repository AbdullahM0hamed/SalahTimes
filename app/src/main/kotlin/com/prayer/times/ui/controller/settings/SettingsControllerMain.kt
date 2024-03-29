package com.prayer.times.ui.controller.settings

import com.bluelinelabs.conductor.RouterTransaction
import com.prayer.times.R
import com.prayer.times.ui.MainActivity
import com.prayer.times.ui.controller.base.SettingsController

class SettingsControllerMain : SettingsController() {

    override fun hasHeader() = true
    override fun hasActionBar() = false
    override fun removeBottomNav() = false
    override fun getSettingsList() = listOf(
        SettingsItemMain(
            resources!!.getString(R.string.settings_general),
            R.drawable.ic_general_24dp,
            { this.router.pushController(RouterTransaction.with(GeneralController())) }
        ),
        SettingsItemMain(
            resources!!.getString(R.string.settings_themes),
            R.drawable.ic_theme_24dp
        ),
        SettingsItemMain(
            resources!!.getString(R.string.settings_relocate),
            R.drawable.ic_relocate_24dp,
            { 
                (activity as? MainActivity)?.locateMe()
            }
        ),
        SettingsItemMain(
            resources!!.getString(R.string.settings_about),
            R.drawable.ic_about_24dp
        )
    )
}
