package com.github.overpass.gather.model.usecase.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.screen.auth.register.signup.SignUpStatus;
import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.register.SignUpRepo;
import com.github.overpass.gather.screen.splash.StartStatus;

public class SignUpUseCase {

    private final SignUpRepo signUpRepo;
    private final BaseValidator validator;
    private final PreferenceRepo preferenceRepo;

    public SignUpUseCase(SignUpRepo signUpRepo, BaseValidator validator, PreferenceRepo preferenceRepo) {
        this.signUpRepo = signUpRepo;
        this.validator = validator;
        this.preferenceRepo = preferenceRepo;
    }

    public LiveData<SignUpStatus> signUp(String email, String password) {
        if (!validator.isEmailValid(email)) {
            MutableLiveData<SignUpStatus> signUpStatus = new MutableLiveData<>();
            signUpStatus.setValue(new SignUpStatus.InvalidEmail("Invalid Email"));
            return signUpStatus;
        }
        if (!validator.isPasswordValid(password)) {
            MutableLiveData<SignUpStatus> signUpStatus = new MutableLiveData<>();
            signUpStatus.setValue(new SignUpStatus.InvalidPassword("Invalid Password"));
            return signUpStatus;
        }
        return signUpRepo.signUp(email, password);
    }

    public void setStartStatus(StartStatus startStatus) {
        preferenceRepo.setStartStatus(startStatus);
    }

    public StartStatus getStartStatus() {
        StartStatus startStatus;
        if (signUpRepo.isUserNull()) {
            startStatus = StartStatus.UNAUTHORIZED;
        } else{
            startStatus = preferenceRepo.getStartStatus();
        }
        return startStatus;
    }
}
