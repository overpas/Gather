package com.github.overpass.gather.screen.auth.register.add;

import android.app.Application;

import androidx.annotation.NonNull;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.model.repo.register.SignUpRepo;
import com.github.overpass.gather.model.usecase.register.SignUpUseCase;
import com.github.overpass.gather.screen.base.personal.DataViewModel;
import com.github.overpass.gather.screen.splash.StartStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPersonalDataViewModel extends DataViewModel {

    private final SignUpUseCase signUpUseCase;

    public AddPersonalDataViewModel(@NonNull Application application) {
        super(application);
        signUpUseCase = new SignUpUseCase(
                new SignUpRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
                new BaseValidator(),
                new PreferenceRepo(application)
        );
    }

    public void setSignUpComplete() {
        signUpUseCase.setStartStatus(StartStatus.AUTHORIZED);
    }
}
