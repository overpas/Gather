package com.github.overpass.gather.auth.register.add;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.SingleLiveEvent;

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
