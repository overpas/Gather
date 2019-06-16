package com.github.overpass.gather.model.usecase.login

import com.github.overpass.gather.model.repo.pref.PreferenceRepo
import com.github.overpass.gather.model.repo.register.SignUpRepo
import com.github.overpass.gather.screen.splash.StartStatus

class StartStatusUseCase(
        private val preferenceRepo: PreferenceRepo,
        private val signUpRepo: SignUpRepo
) {

    fun setStartStatus(startStatus: StartStatus) {
        preferenceRepo.startStatus = startStatus
    }

    fun getStartStatus(): StartStatus = if (signUpRepo.isUserNull()) {
        StartStatus.UNAUTHORIZED
    } else {
        preferenceRepo.startStatus
    }
}