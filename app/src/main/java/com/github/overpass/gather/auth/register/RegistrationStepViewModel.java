package com.github.overpass.gather.auth.register;

import androidx.lifecycle.ViewModel;

public class RegistrationStepViewModel extends ViewModel {

    public void moveToNextStep(RegistrationController registrationController) {
        registrationController.moveToNextStep();
    }
}
