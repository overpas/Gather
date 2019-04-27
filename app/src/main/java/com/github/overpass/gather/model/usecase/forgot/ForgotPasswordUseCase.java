package com.github.overpass.gather.model.usecase.forgot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.auth.login.forgot.ForgotStatus;
import com.github.overpass.gather.model.repo.password.PasswordResetRepo;

public class ForgotPasswordUseCase {

    private final PasswordResetRepo passwordResetRepo;
    private final BaseValidator validator;

    public ForgotPasswordUseCase(PasswordResetRepo passwordResetRepo, BaseValidator validator) {
        this.passwordResetRepo = passwordResetRepo;
        this.validator = validator;
    }

    public LiveData<ForgotStatus> sendForgotPassword(String email) {
        if (validator.isEmailValid(email)) {
            return passwordResetRepo.sendForgotPassword(email);
        } else {
            MutableLiveData<ForgotStatus> result = new MutableLiveData<>();
            result.setValue(new ForgotStatus.InvalidEmail());
            return result;
        }
    }
}
