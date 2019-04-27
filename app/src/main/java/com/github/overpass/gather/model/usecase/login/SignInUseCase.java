package com.github.overpass.gather.model.usecase.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.auth.login.SignInStatus;
import com.github.overpass.gather.model.repo.login.SignInRepo;

public class SignInUseCase {

    private final SignInRepo signInRepo;
    private final BaseValidator validator;

    public SignInUseCase(SignInRepo signInRepo, BaseValidator credsValidator) {
        this.signInRepo = signInRepo;
        this.validator = credsValidator;
    }

    public LiveData<SignInStatus> signIn(String email, String password) {
        if (!validator.isEmailValid(email)) {
            MutableLiveData<SignInStatus> signInStatus = new MutableLiveData<>();
            signInStatus.setValue(new SignInStatus.InvalidEmail("Invalid Email"));
            return signInStatus;
        }
        if (!validator.isPasswordValid(password)) {
            MutableLiveData<SignInStatus> signInStatus = new MutableLiveData<>();
            signInStatus.setValue(new SignInStatus.InvalidPassword("Invalid Password"));
            return signInStatus;
        }
        return signInRepo.signIn(email, password);
    }
}
