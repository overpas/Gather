package com.github.overpass.gather.auth.login.forgot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.password.PasswordResetRepo;
import com.github.overpass.gather.model.usecase.forgot.ForgotPasswordUseCase;
import com.google.firebase.auth.FirebaseAuth;

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
