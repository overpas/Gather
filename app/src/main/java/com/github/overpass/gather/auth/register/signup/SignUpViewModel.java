package com.github.overpass.gather.auth.register.signup;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.BaseValidator;
import com.github.overpass.gather.auth.register.RegistrationStepViewModel;
import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.LiveData;

public class SignUpViewModel extends RegistrationStepViewModel {

    private final SignUpUseCase signUpUseCase;
    private final SingleLiveEvent<SignUpStatus> signUpData;

    public SignUpViewModel() {
        signUpUseCase = new SignUpUseCase(
                new SignUpRepo(FirebaseAuth.getInstance()),
                new BaseValidator()
        );
        signUpData = new SingleLiveEvent<>();
    }

    public void signUp(String email, String password) {
        signUpUseCase.signUp(signUpData, email, password);
    }

    public LiveData<SignUpStatus> getSignUpData() {
        return signUpData;
    }
}
