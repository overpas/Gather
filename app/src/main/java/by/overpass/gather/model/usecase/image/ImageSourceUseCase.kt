package by.overpass.gather.model.usecase.image

import androidx.lifecycle.LiveData
import by.overpass.gather.ui.auth.register.add.ImageSource
import com.hadilq.liveevent.LiveEvent

class ImageSourceUseCase constructor(
        private val imageSourceData: LiveEvent<ImageSource> = LiveEvent()
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
