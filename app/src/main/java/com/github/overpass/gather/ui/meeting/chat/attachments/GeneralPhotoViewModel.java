package com.github.overpass.gather.ui.meeting.chat.attachments;

import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.ui.base.imagesource.IImageSourceViewModel;

import javax.inject.Inject;

public class GeneralPhotoViewModel extends ViewModel implements IImageSourceViewModel {

    private final ImageSourceUseCase imageSourceUseCase;

    @Inject
    public GeneralPhotoViewModel(ImageSourceUseCase imageSourceUseCase) {
        this.imageSourceUseCase = imageSourceUseCase;
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
