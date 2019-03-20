package com.github.overpass.gather.auth.register;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.BaseValidator;
import com.github.overpass.gather.auth.login.SignInStatus;

public class SignUpUseCase {

    private final SignUpRepo signUpRepo;
    private final BaseValidator validator;

    public SignUpUseCase(SignUpRepo signUpRepo, BaseValidator validator) {
        this.signUpRepo = signUpRepo;
        this.validator = validator;
    }

    public void signUp(SingleLiveEvent<SignUpStatus> signUpData, String email, String password) {
        if (!validator.isEmailValid(email)) {
            signUpData.setValue(new SignUpStatus.InvalidEmail("Invalid Email"));
            return;
        }
        if (!validator.isPasswordValid(password)) {
            signUpData.setValue(new SignUpStatus.InvalidPassword("Invalid Password"));
            return;
        }
        signUpRepo.signUp(signUpData, email, password);
    }
}
