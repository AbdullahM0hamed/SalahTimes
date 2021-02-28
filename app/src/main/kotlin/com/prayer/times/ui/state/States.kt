package com.prayer.times.ui.state

import com.prayer.times.Constants
import java.util.Date

data class AppState(
    val salahTimesState: SalahTimesState = SalahTimesState(),
    val compassState: CompassState = CompassState()
)

data class SalahTimesState(
    val date: Date = Date()
)

data class CompassState(
    val northAngle: Float = 0f,
    val qiblahAngle: Float = 0f,
    val sunState: Int = Constants.SUN_NONE
)
