package com.github.overpass.gather.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import com.github.overpass.gather.model.data.entity.splash.StartStatus
import com.github.overpass.gather.model.usecase.login.StartStatusUseCase
import javax.inject.Inject

class SplashViewModel @Inject constructor(
        application: Application,
        private val startStatusUseCase: StartStatusUseCase,
        private val authorizedData: SingleLiveEvent<Void>,
        private val unauthorizedData: SingleLiveEvent<Void>,
        private val notAddedDataData: SingleLiveEvent<Void>,
        private val unconfirmedEmailData: SingleLiveEvent<Void>
) : AndroidViewModel(application) {

    fun onSplashAnimationComplete() {
        when (startStatusUseCase.getStartStatus()) {
            StartStatus.AUTHORIZED -> authorizedData.call()
            StartStatus.UNAUTHORIZED -> unauthorizedData.call()
            StartStatus.NOT_ADDED_DATA -> notAddedDataData.call()
            StartStatus.UNCONFIRMED_EMAIL -> unconfirmedEmailData.call()
        }
    }

    fun authorized(): LiveData<Void> = authorizedData

    fun unauthorized(): LiveData<Void> = unauthorizedData

    fun notAddedData(): LiveData<Void> = notAddedDataData

    fun unconfirmedEmail(): LiveData<Void> = unconfirmedEmailData
}
