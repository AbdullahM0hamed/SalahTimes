package com.prayer.times

class Constants {
    companion object {
        const val LATITUDE = "pref_latitude"
        const val LONGITUDE = "pref_longitude"
        const val MAP_RESULT = 123
        const val SET_PRAYER_REMINDER = "intent.action.SET_PRAYER_REMINDER"
        const val SALAH_NAME = "pref_salah_name"
        const val SUN_NONE = 0
        const val SUN_EAST = 1
        const val SUN_WEST = 2

        // Preference keys
        const val LANGUAGE_KEY = "language"
        const val CALCULATION_KEY = "calculation_method"
        const val MADHAB_KEY = "madhab"
        const val TIME_FORMAT_KEY = "time_format"

        // Preference defaults
        const val LANGUAGE_DEFAULT = "english"
        const val CALCULATION_DEFAULT = "moonsighting"
        const val MADHAB_DEFAULT = "shafi"
        const val TIME_FORMAT_DEFAULT = "24_hour_time"
    }
}
