package com.github.overpass.gather.auth.register.signup;

import com.github.overpass.gather.auth.BaseValidator;
import com.github.overpass.gather.auth.register.RegistrationStepViewModel;
import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.LiveData;

public class SignUpViewModel extends RegistrationStepViewModel {

    private final SignUpUseCase signUpUseCase;

    public SignUpViewModel() {
        signUpUseCase = new SignUpUseCase(
                new SignUpRepo(FirebaseAuth.getInstance()),
                new BaseValidator()
        );
    }

    public LiveData<SignUpStatus> signUp2(String email, String password) {
        return signUpUseCase.signUp(email, password);
    }
}
