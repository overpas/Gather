package com.github.overpass.gather.auth.login.forgot;

import com.github.overpass.gather.auth.BaseValidator;
import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ForgotPasswordViewModel extends ViewModel {

    private final ForgotPasswordUseCase forgotPasswordUseCase;

    public ForgotPasswordViewModel() {
        forgotPasswordUseCase = new ForgotPasswordUseCase(new PasswordResetRepo(
                FirebaseAuth.getInstance()), new BaseValidator());
    }

    public LiveData<ForgotStatus> sendForgotPassword(String email) {
        return forgotPasswordUseCase.sendForgotPassword(email);
    }
}
