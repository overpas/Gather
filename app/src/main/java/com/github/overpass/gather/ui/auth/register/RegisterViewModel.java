package com.github.overpass.gather.ui.auth.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.commons.android.lifecycle.SingleLiveEvent;
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.ui.base.imagesource.IImageSourceViewModel;

import javax.inject.Inject;

public class RegisterViewModel extends ViewModel implements IImageSourceViewModel {

    private final ImageSourceUseCase imageSourceUseCase;
    private final SingleLiveEvent<Integer> registrationData;

    @Inject
    public RegisterViewModel(Integer initialStep,
                             ImageSourceUseCase imageSourceUseCase,
                             SingleLiveEvent<Integer> registrationData) {
        this.imageSourceUseCase = imageSourceUseCase;
        this.registrationData = registrationData;
        this.registrationData.setValue(initialStep);
    }

    @SuppressWarnings("ConstantConditions")
    void next() {
        int current = registrationData.getValue();
        registrationData.setValue(current + 1);
    }

    LiveData<Integer> getRegistrationProgressData() {
        return registrationData;
    }

    boolean shouldShowNextStep(Integer step) {
        return step < 3 && step != 0;
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
