package com.prayer.times.ui.controller.settings

import com.prayer.times.R
import com.prayer.times.ui.controller.base.SettingsController

class GeneralController : SettingsController() {

    override fun getSettingsList() = listOf(
        GeneralItem(
            resources!!.getString(R.string.language_title),
            resources!!.getString(R.string.language_default),
            { android.widget.Toast.makeText(activity as android.content.Context, "Test", 5).show() }
        )
    )
}
