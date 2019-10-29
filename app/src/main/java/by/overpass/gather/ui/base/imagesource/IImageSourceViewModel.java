package by.overpass.gather.ui.base.imagesource;


import by.overpass.gather.model.usecase.image.ImageSourceUseCase;

public interface IImageSourceViewModel {

    ImageSourceUseCase getImageSourceUseCase();

    void onGallery();

    void onCamera();
}
