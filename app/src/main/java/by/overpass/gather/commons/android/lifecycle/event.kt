package by.overpass.gather.commons.android.lifecycle

import androidx.lifecycle.LiveData
import com.hadilq.liveevent.LiveEvent

typealias SimpleLiveEvent = LiveEvent<Unit>

fun SimpleLiveEvent.trigger() {
    value = Unit
}

typealias JustLiveData = LiveData<Unit>