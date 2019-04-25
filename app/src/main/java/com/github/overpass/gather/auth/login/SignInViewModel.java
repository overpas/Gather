package com.github.overpass.gather.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.auth.BaseValidator;

public class SignInViewModel extends ViewModel {

    private final SignInUseCase signInUseCase;

    public SignInViewModel() {
        signInUseCase = new SignInUseCase(new SignInRepo(), new BaseValidator());
    }

    public LiveData<SignInStatus> signIn(String email, String password) {
        return signInUseCase.signIn(email, password);
    }

}
