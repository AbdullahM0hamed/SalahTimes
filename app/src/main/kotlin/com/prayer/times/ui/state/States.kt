package com.prayer.times.ui.state

import java.util.Date

data class AppState(
    val salahTimesState: SalahTimesState = SalahTimesState()
)

data class SalahTimesState(
    val date: Date = Date()
)
