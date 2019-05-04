package com.github.overpass.gather.screen.auth.register;

public interface RegistrationController {

    void moveToNextStep();
    int getInitialStep();
}