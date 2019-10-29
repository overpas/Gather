package by.overpass.gather.ui.profile;

import androidx.lifecycle.ViewModel;

import by.overpass.gather.model.usecase.image.ImageSourceUseCase;
import by.overpass.gather.ui.base.imagesource.IImageSourceViewModel;

import javax.inject.Inject;

public class GeneralProfileViewModel extends ViewModel implements IImageSourceViewModel {

    private final ImageSourceUseCase imageSourceUseCase;

    @Inject
    public GeneralProfileViewModel(ImageSourceUseCase imageSourceUseCase) {
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
