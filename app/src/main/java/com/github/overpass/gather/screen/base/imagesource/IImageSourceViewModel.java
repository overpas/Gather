package com.github.overpass.gather.screen.base.imagesource;

import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;

public interface IImageSourceViewModel {

    ImageSourceUseCase getImageSourceUseCase();

    void onGallery();

    void onCamera();
}
