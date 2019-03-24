package com.github.overpass.gather.auth.register.confirm;

import com.github.overpass.gather.SingleLiveEvent;
import com.github.overpass.gather.auth.register.RegistrationStepViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class ConfirmEmailViewModel extends RegistrationStepViewModel {

    private final SingleLiveEvent<ConfirmEmailStatus> confirmEmailData;
    private final ConfirmEmailUseCase confirmEmailUseCase;

    public ConfirmEmailViewModel() {
        confirmEmailUseCase = new ConfirmEmailUseCase(
                new ConfirmEmailRepo(FirebaseAuth.getInstance())
        );
        confirmEmailData = new SingleLiveEvent<>();
    }

    public void confirm() {
        confirmEmailUseCase.confirmEmail(confirmEmailData);
    }

    public SingleLiveEvent<ConfirmEmailStatus> getConfirmationData() {
        return confirmEmailData;
    }
}
