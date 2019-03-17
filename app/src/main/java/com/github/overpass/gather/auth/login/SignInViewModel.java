package com.github.overpass.gather.auth.login;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.CredsValidator;

import androidx.lifecycle.ViewModel;

public class SignInViewModel extends ViewModel {

    private SignInUseCase signInUseCase = new SignInUseCase(new SignInRepo(), new CredsValidator());
    private SingleLiveEvent<SignInStatus> signInData = new SingleLiveEvent<>();

    public SingleLiveEvent<SignInStatus> getSignInData() {
        return signInData;
    }

    public void signIn(String email, String password) {
        signInUseCase.signIn(signInData, email, password);
    }

}
