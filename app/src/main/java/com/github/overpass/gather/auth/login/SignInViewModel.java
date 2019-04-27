package com.github.overpass.gather.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.login.SignInRepo;
import com.github.overpass.gather.model.usecase.login.SignInUseCase;

public class SignInViewModel extends ViewModel {

    private final SignInUseCase signInUseCase;

    public SignInViewModel() {
        signInUseCase = new SignInUseCase(new SignInRepo(), new BaseValidator());
    }

    public LiveData<SignInStatus> signIn(String email, String password) {
        return signInUseCase.signIn(email, password);
    }

}
