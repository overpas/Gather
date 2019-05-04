package com.github.overpass.gather.screen.auth.register.confirm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.model.repo.register.SignUpRepo;
import com.github.overpass.gather.model.usecase.register.SignUpUseCase;
import com.github.overpass.gather.screen.auth.register.RegistrationStepViewModel;
import com.github.overpass.gather.model.repo.confirm.ConfirmEmailRepo;
import com.github.overpass.gather.model.usecase.confirm.ConfirmEmailUseCase;
import com.github.overpass.gather.screen.splash.StartStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfirmEmailViewModel extends RegistrationStepViewModel {

    private final ConfirmEmailUseCase confirmEmailUseCase;
    private final SignUpUseCase signUpUseCase;

    public ConfirmEmailViewModel(@NonNull Application application) {
        super(application);
        confirmEmailUseCase = new ConfirmEmailUseCase(
                new ConfirmEmailRepo(FirebaseAuth.getInstance())
        );
        signUpUseCase = new SignUpUseCase(
                new SignUpRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
                new BaseValidator(),
                new PreferenceRepo(application)
        );
    }

    public LiveData<ConfirmEmailStatus> confirm() {
        return confirmEmailUseCase.confirmEmail();
    }

    public void setEmailConfirmed() {
        signUpUseCase.setStartStatus(StartStatus.NOT_ADDED_DATA);
    }
}
