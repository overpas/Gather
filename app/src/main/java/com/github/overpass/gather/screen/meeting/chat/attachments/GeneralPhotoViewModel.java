package com.github.overpass.gather.screen.meeting.chat.attachments;

import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.screen.base.imagesource.IImageSourceViewModel;

public class GeneralPhotoViewModel extends ViewModel implements IImageSourceViewModel {

    private final ImageSourceUseCase imageSourceUseCase;

    public GeneralPhotoViewModel() {
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
