package by.overpass.gather.ui.meeting.enrolled


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import javax.inject.Inject

class EnrolledViewModel @Inject constructor(
        private val playAnimationData: SingleLiveEvent<Void>
) : ViewModel() {

    init {
        playAnimationData.call()
    }

    fun playAnimation(): LiveData<Void> = playAnimationData
}
