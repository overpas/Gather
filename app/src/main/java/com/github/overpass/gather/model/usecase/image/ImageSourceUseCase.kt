package com.github.overpass.gather.model.usecase.image

import androidx.lifecycle.LiveData

import com.github.overpass.gather.screen.auth.register.add.ImageSource
import com.github.overpass.gather.model.commons.SingleLiveEvent

class ImageSourceUseCase {

    private val imageSourceData: SingleLiveEvent<ImageSource> = SingleLiveEvent()

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
