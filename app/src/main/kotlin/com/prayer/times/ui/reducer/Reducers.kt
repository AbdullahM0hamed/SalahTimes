package com.prayer.times.ui.reducer

import com.prayer.times.ui.action.AddToDate
import com.prayer.times.ui.action.SetRotation
import com.prayer.times.ui.action.UpdateSunState
import com.prayer.times.ui.action.UpdateTimes
import com.prayer.times.ui.state.AppState
import com.prayer.times.ui.state.CompassState
import com.prayer.times.ui.state.SalahTimesState
import java.util.Calendar
import java.util.Date

fun appStateReducer(state: AppState, action: Any) = AppState(
    salahTimesState = salahTimesStateReducer(state.salahTimesState, action),
    compassState = compassStateReducer(state.compassState, action)
)

fun salahTimesStateReducer(state: SalahTimesState, action: Any): SalahTimesState {
    var currentState = state
    when (action) {
        is UpdateTimes -> { currentState = state.copy(action.date) }
        is AddToDate -> { 
            val cal = Calendar.getInstance()
            cal.setTime(state.date)
            cal.add(Calendar.DATE, action.days)
            currentState = state.copy(cal.getTime())
        }
    }

    return currentState
}

fun compassStateReducer(state: CompassState, action: Any): CompassState {
    var currentState = state
    
    when (action) {
        is SetRotation -> { currentState = currentState.copy(northAngle = action.northAngle + state.northAngle, qiblahAngle = action.qiblahAngle + state.qiblahAngle) }
        is UpdateSunState -> { currentState = currentState.copy(sunState = action.sunState) }
    }

    return currentState
}
