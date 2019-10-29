package com.github.overpass.gather.screen.meeting.enrolled


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.overpass.gather.model.commons.SingleLiveEvent
import javax.inject.Inject

class EnrolledViewModel @Inject constructor(
        private val playAnimationData: SingleLiveEvent<Void>
) : ViewModel() {

    init {
        playAnimationData.call()
    }

    fun playAnimation(): LiveData<Void> = playAnimationData
}
