package com.github.overpass.gather.auth.register;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.BaseValidator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {

    private final SignUpUseCase signUpUseCase = new SignUpUseCase(new SignUpRepo(),
            new BaseValidator());
    private final SingleLiveEvent<SignUpStatus> signUpData = new SingleLiveEvent<>();

    public void signUp(String email, String password) {
        signUpUseCase.signUp(signUpData, email, password);
    }

    public LiveData<SignUpStatus> getSignUpData() {
        return signUpData;
    }
}
