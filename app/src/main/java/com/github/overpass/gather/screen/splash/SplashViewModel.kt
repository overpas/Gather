package com.github.overpass.gather.screen.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.overpass.gather.model.commons.SingleLiveEvent
import com.github.overpass.gather.model.data.entity.splash.StartStatus
import com.github.overpass.gather.model.repo.pref.PreferenceRepo
import com.github.overpass.gather.model.repo.register.SignUpRepo
import com.github.overpass.gather.model.usecase.login.StartStatusUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val startStatusUseCase: StartStatusUseCase = StartStatusUseCase(
            PreferenceRepo(application),
            SignUpRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    )
    private val authorizedData: SingleLiveEvent<Void> = SingleLiveEvent()
    private val unauthorizedData: SingleLiveEvent<Void> = SingleLiveEvent()
    private val notAddedDataData: SingleLiveEvent<Void> = SingleLiveEvent()
    private val unconfirmedEmailData: SingleLiveEvent<Void> = SingleLiveEvent()

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
