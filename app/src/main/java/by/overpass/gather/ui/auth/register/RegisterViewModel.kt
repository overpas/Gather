package by.overpass.gather.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.overpass.gather.model.usecase.image.ImageSourceUseCase
import by.overpass.gather.ui.base.imagesource.IImageSourceViewModel
import com.hadilq.liveevent.LiveEvent
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
        initialStep: Int,
        private val imageSourceUseCase: ImageSourceUseCase,
        private val registrationData: LiveEvent<Int>
) : ViewModel(), IImageSourceViewModel {

    init {
        this.registrationData.value = initialStep
    }

    fun getRegistrationProgressData(): LiveData<Int> = registrationData

    fun next() {
        val current = registrationData.value!!
        registrationData.value = current + 1
    }

    fun shouldShowNextStep(step: Int): Boolean {
        return step < 3 && step != 0
    }

    override fun getImageSourceUseCase(): ImageSourceUseCase {
        return imageSourceUseCase
    }

    override fun onGallery() {
        imageSourceUseCase.onGallery()
    }

    override fun onCamera() {
        imageSourceUseCase.onCamera()
    }
}
