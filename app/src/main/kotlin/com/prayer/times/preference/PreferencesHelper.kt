package com.prayer.times.preference

import android.content.Context
import androidx.preference.PreferenceManager

class PreferencesHelper(val context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getString(key: String) = prefs.getString(key, "")

    fun putString(key: String, value: String) {
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }
}
