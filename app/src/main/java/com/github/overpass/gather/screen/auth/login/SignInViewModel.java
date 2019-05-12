package com.github.overpass.gather.screen.auth.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.login.AuthRepo;
import com.github.overpass.gather.model.repo.pref.PreferenceRepo;
import com.github.overpass.gather.model.usecase.login.SignInUseCase;
import com.google.firebase.auth.FirebaseAuth;

public class SignInViewModel extends AndroidViewModel {

    private final SignInUseCase signInUseCase;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        signInUseCase = new SignInUseCase(
                new AuthRepo(FirebaseAuth.getInstance()),
                new BaseValidator(),
                new PreferenceRepo(application)
        );
    }

    public LiveData<SignInStatus> signIn(String email, String password) {
        return signInUseCase.signIn(email, password);
    }

}
