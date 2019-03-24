package com.github.overpass.gather.auth.register.signup;

import com.github.overpass.gather.auth.BaseValidator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SignUpUseCase {

    private final SignUpRepo signUpRepo;
    private final BaseValidator validator;

    SignUpUseCase(SignUpRepo signUpRepo, BaseValidator validator) {
        this.signUpRepo = signUpRepo;
        this.validator = validator;
    }

    LiveData<SignUpStatus> signUp(String email, String password) {
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

}
