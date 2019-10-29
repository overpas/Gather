package com.github.overpass.gather.model.usecase.login

import com.github.overpass.gather.model.data.entity.splash.StartStatus
import com.github.overpass.gather.model.repo.pref.PreferenceRepo
import com.github.overpass.gather.model.repo.register.SignUpRepo
import javax.inject.Inject

class StartStatusUseCase @Inject constructor(
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