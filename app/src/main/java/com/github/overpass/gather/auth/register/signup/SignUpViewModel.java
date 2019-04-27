package com.github.overpass.gather.auth.register.signup;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.auth.register.RegistrationStepViewModel;
import com.github.overpass.gather.model.repo.register.SignUpRepo;
import com.github.overpass.gather.model.usecase.register.SignUpUseCase;
import com.google.firebase.auth.FirebaseAuth;

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
