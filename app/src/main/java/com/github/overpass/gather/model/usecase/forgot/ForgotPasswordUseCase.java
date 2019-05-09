package com.github.overpass.gather.model.usecase.forgot;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.commons.LiveDataUtils;
import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.password.PasswordResetRepo;
import com.github.overpass.gather.screen.auth.login.forgot.ForgotStatus;

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
            return LiveDataUtils.just(new ForgotStatus.InvalidEmail());
        }
    }
}
