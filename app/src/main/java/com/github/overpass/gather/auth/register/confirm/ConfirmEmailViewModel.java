package com.github.overpass.gather.auth.register.confirm;

import com.github.overpass.gather.auth.register.RegistrationStepViewModel;
import com.google.firebase.auth.FirebaseAuth;

import androidx.lifecycle.LiveData;

public class ConfirmEmailViewModel extends RegistrationStepViewModel {

    private final ConfirmEmailUseCase confirmEmailUseCase;

    public ConfirmEmailViewModel() {
        confirmEmailUseCase = new ConfirmEmailUseCase(
                new ConfirmEmailRepo(FirebaseAuth.getInstance())
        );
    }

    public LiveData<ConfirmEmailStatus> confirm() {
        return confirmEmailUseCase.confirmEmail();
    }
}
