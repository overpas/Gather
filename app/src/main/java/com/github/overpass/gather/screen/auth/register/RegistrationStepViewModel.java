package com.github.overpass.gather.screen.auth.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class RegistrationStepViewModel extends AndroidViewModel {

    public RegistrationStepViewModel(@NonNull Application application) {
        super(application);
    }

    public void moveToNextStep(RegistrationController registrationController) {
        registrationController.moveToNextStep();
    }
}
