package com.github.overpass.gather.auth.register;

import com.github.overpass.gather.SingleLiveEvent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private SingleLiveEvent<Integer> registrationData = new SingleLiveEvent<>();

    {
        registrationData.postValue(0);
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
}
