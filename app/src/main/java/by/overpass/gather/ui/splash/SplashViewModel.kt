package by.overpass.gather.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import by.overpass.gather.commons.android.lifecycle.JustLiveData
import by.overpass.gather.commons.android.lifecycle.SimpleLiveEvent
import by.overpass.gather.commons.android.lifecycle.trigger
import by.overpass.gather.model.entity.splash.StartStatus
import by.overpass.gather.model.usecase.login.StartStatusUseCase
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        application: Application,
        private val startStatusUseCase: StartStatusUseCase,
        private val authorizedData: SimpleLiveEvent,
        private val unauthorizedData: SimpleLiveEvent,
        private val notAddedDataData: SimpleLiveEvent,
        private val unconfirmedEmailData: SimpleLiveEvent
) : AndroidViewModel(application) {

    fun onSplashAnimationComplete() {
        when (startStatusUseCase.getStartStatus()) {
            StartStatus.AUTHORIZED -> authorizedData.trigger()
            StartStatus.UNAUTHORIZED -> unauthorizedData.trigger()
            StartStatus.NOT_ADDED_DATA -> notAddedDataData.trigger()
            StartStatus.UNCONFIRMED_EMAIL -> unconfirmedEmailData.trigger()
        }
    }

    fun authorized(): JustLiveData = authorizedData

    fun unauthorized(): JustLiveData = unauthorizedData

    fun notAddedData(): JustLiveData = notAddedDataData

    fun unconfirmedEmail(): JustLiveData = unconfirmedEmailData
}
