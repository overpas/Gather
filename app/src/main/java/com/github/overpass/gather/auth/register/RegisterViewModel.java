package com.github.overpass.gather.auth.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.commons.SingleLiveEvent;
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;

public class RegisterViewModel extends ViewModel {

    private final ImageSourceUseCase imageSourceUseCase;
    private final SingleLiveEvent<Integer> registrationData;

    public RegisterViewModel() {
        imageSourceUseCase = new ImageSourceUseCase();
        registrationData = new SingleLiveEvent<>();
        registrationData.setValue(0);
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
