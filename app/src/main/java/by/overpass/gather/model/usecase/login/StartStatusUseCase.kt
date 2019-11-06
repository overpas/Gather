package by.overpass.gather.model.usecase.login

import by.overpass.gather.model.entity.splash.StartStatus
import by.overpass.gather.data.repo.pref.PreferenceRepo
import by.overpass.gather.data.repo.register.SignUpRepo
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