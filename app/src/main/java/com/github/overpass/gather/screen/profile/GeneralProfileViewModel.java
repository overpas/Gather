package com.github.overpass.gather.screen.profile;

import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.screen.base.imagesource.IImageSourceViewModel;

public class GeneralProfileViewModel extends ViewModel implements IImageSourceViewModel {

    private final ImageSourceUseCase imageSourceUseCase;

    public GeneralProfileViewModel() {
        this.imageSourceUseCase = new ImageSourceUseCase();
    }

    public ImageSourceUseCase getImageSourceUseCase() {
        return imageSourceUseCase;
    }

    public void onGallery() {
        imageSourceUseCase.onGallery();
    }

    public void onCamera() {
        imageSourceUseCase.onCamera();
    }
}
