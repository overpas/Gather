package com.github.overpass.gather.auth.register.signup;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.BaseValidator;
import com.github.overpass.gather.auth.register.RegistrationStepViewModel;

import androidx.lifecycle.LiveData;

public class SignUpViewModel extends RegistrationStepViewModel {

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
