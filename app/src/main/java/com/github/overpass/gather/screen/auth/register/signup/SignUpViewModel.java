package com.github.overpass.gather.screen.auth.register.signup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.screen.auth.register.RegistrationStepViewModel;
import com.github.overpass.gather.model.repo.register.SignUpRepo;
import com.github.overpass.gather.model.usecase.register.SignUpUseCase;
import com.github.overpass.gather.screen.splash.StartStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpViewModel extends RegistrationStepViewModel {

    private final SignUpUseCase signUpUseCase;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signUpUseCase = new SignUpUseCase(
                new SignUpRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()),
                new BaseValidator(),
                new PreferenceRepo(application.getApplicationContext())
        );
    }

    public LiveData<SignUpStatus> signUp(String email, String password) {
        return signUpUseCase.signUp(email, password);
    }

    public void setSignUpInProgress() {
        signUpUseCase.setStartStatus(StartStatus.UNCONFIRMED_EMAIL);
    }
}
