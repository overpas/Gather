package by.overpass.gather.model.usecase.image

import androidx.lifecycle.LiveData
import by.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import by.overpass.gather.ui.auth.register.add.ImageSource

class ImageSourceUseCase constructor(
        private val imageSourceData: SingleLiveEvent<ImageSource> = SingleLiveEvent()
) {

    fun getImageSourceData(): LiveData<ImageSource> {
        return imageSourceData
    }

    fun onGallery() {
        imageSourceData.value = ImageSource.GALLERY
    }

    fun onCamera() {
        imageSourceData.value = ImageSource.CAMERA
    }
}
