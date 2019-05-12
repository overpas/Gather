package com.github.overpass.gather.model.usecase.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.commons.LiveDataUtils;
import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.screen.auth.login.SignInStatus;
import com.github.overpass.gather.model.repo.login.AuthRepo;
import com.github.overpass.gather.screen.splash.StartStatus;

public class SignInUseCase {

    private final AuthRepo authRepo;
    private final BaseValidator validator;
    private final PreferenceRepo preferenceRepo;

    public SignInUseCase(AuthRepo authRepo,
                         BaseValidator credsValidator,
                         PreferenceRepo preferenceRepo) {
        this.authRepo = authRepo;
        this.validator = credsValidator;
        this.preferenceRepo = preferenceRepo;
    }

    public LiveData<SignInStatus> signIn(String email, String password) {
        if (!validator.isEmailValid(email)) {
            return LiveDataUtils.just(new SignInStatus.InvalidEmail("Invalid Email"));
        }
        if (!validator.isPasswordValid(password)) {
            return LiveDataUtils.just(new SignInStatus.InvalidPassword("Invalid Password"));
        }
        return Transformations.map(authRepo.signIn(email, password), status -> {
            preferenceRepo.setStartStatus(StartStatus.AUTHORIZED);
            return status;
        });
    }
}
