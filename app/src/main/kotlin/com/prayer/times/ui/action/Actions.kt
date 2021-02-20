package com.prayer.times.ui.action

import java.util.Date

data class UpdateTimes(val date: Date = Date())
data class AddToDate(val days: Int)
