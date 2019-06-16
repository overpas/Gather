package com.github.overpass.gather.model.usecase.register;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.commons.LiveDataUtils;
import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.register.SignUpRepo;
import com.github.overpass.gather.screen.auth.register.signup.SignUpStatus;

public class SignUpUseCase {

    private final SignUpRepo signUpRepo;
    private final BaseValidator validator;

    public SignUpUseCase(SignUpRepo signUpRepo, BaseValidator validator) {
        this.signUpRepo = signUpRepo;
        this.validator = validator;
    }

    public LiveData<SignUpStatus> signUp(String email, String password) {
        if (!validator.isEmailValid(email)) {
            return LiveDataUtils.just(new SignUpStatus.InvalidEmail("Invalid Email"));
        }
        if (!validator.isPasswordValid(password)) {
            return LiveDataUtils.just(new SignUpStatus.InvalidPassword("Invalid Password"));
        }
        return signUpRepo.signUp(email, password);
    }
}
