package com.prayer.times.ui.controller.settings

import android.content.Context
import com.prayer.times.R
import com.prayer.times.ui.controller.base.SettingsController

class SettingsControllerMain : SettingsController() {

    private val context = activity as Context
    
    override fun getSettingsList() = listOf(
        SettingsItemMain(
            context.getString(R.string.settings_general),
            R.drawable.ic_general_24dp,
            this
        )
    )
}
