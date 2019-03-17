package com.github.overpass.gather.auth.login;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.CredsValidator;

public class SignInUseCase {

    private final SignInRepo signInRepo;
    private final CredsValidator validator;

    public SignInUseCase(SignInRepo signInRepo, CredsValidator credsValidator) {
        this.signInRepo = signInRepo;
        this.validator = credsValidator;
    }

    public void signIn(SingleLiveEvent<SignInStatus> signInStatus, String email, String password) {
        if (!validator.isEmailValid(email)) {
            signInStatus.setValue(new SignInStatus.InvalidEmail("Invalid Email"));
            return;
        }
        if (!validator.isPasswordValid(password)) {
            signInStatus.setValue(new SignInStatus.InvalidPassword("Invalid Password"));
            return;
        }
        signInRepo.signIn(signInStatus, email, password);
    }
}
