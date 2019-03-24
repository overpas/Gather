package com.github.overpass.gather.auth.login;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.BaseValidator;

import androidx.lifecycle.ViewModel;

public class SignInViewModel extends ViewModel {

    private final SignInUseCase signInUseCase;
    private final SingleLiveEvent<SignInStatus> signInData;

    public SignInViewModel() {
        signInUseCase = new SignInUseCase(new SignInRepo(), new BaseValidator());
        signInData = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<SignInStatus> getSignInData() {
        return signInData;
    }

    public void signIn(String email, String password) {
        signInUseCase.signIn(signInData, email, password);
    }

}
