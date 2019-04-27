package com.github.overpass.gather.auth.register.confirm;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.auth.register.RegistrationStepViewModel;
import com.github.overpass.gather.model.repo.confirm.ConfirmEmailRepo;
import com.github.overpass.gather.model.usecase.confirm.ConfirmEmailUseCase;
import com.google.firebase.auth.FirebaseAuth;

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
