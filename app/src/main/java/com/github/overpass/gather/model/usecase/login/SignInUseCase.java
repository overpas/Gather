package com.github.overpass.gather.model.usecase.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.commons.LiveDataUtils;
import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.screen.auth.login.SignInStatus;
import com.github.overpass.gather.model.repo.login.AuthRepo;

public class SignInUseCase {

    private final AuthRepo authRepo;
    private final BaseValidator validator;

    public SignInUseCase(AuthRepo authRepo, BaseValidator credsValidator) {
        this.authRepo = authRepo;
        this.validator = credsValidator;
    }

    public LiveData<SignInStatus> signIn(String email, String password) {
        if (!validator.isEmailValid(email)) {
            return LiveDataUtils.just(new SignInStatus.InvalidEmail("Invalid Email"));
        }
        if (!validator.isPasswordValid(password)) {
            return LiveDataUtils.just(new SignInStatus.InvalidPassword("Invalid Password"));
        }
        return authRepo.signIn(email, password);
    }
}
