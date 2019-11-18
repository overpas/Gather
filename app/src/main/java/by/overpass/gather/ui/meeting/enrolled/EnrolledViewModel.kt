package by.overpass.gather.ui.meeting.enrolled


import androidx.lifecycle.ViewModel
import by.overpass.gather.commons.android.lifecycle.JustLiveData
import by.overpass.gather.commons.android.lifecycle.SimpleLiveEvent
import by.overpass.gather.commons.android.lifecycle.trigger
import javax.inject.Inject

class EnrolledViewModel @Inject constructor(
        private val playAnimationData: SimpleLiveEvent
) : ViewModel() {

    init {
        playAnimationData.trigger()
    }

    fun playAnimation(): JustLiveData = playAnimationData
}
