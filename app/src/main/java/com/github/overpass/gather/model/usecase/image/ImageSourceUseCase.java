package com.github.overpass.gather.model.usecase.image;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.auth.register.add.ImageSource;
import com.github.overpass.gather.model.commons.SingleLiveEvent;

public class ImageSourceUseCase {

    private final SingleLiveEvent<ImageSource> imageSourceData;

    public ImageSourceUseCase() {
        imageSourceData = new SingleLiveEvent<>();
    }

    public LiveData<ImageSource> getImageSourceData() {
        return imageSourceData;
    }

    public void onGallery() {
        imageSourceData.setValue(ImageSource.GALLERY);
    }

    public void onCamera() {
        imageSourceData.setValue(ImageSource.CAMERA);
    }
}
