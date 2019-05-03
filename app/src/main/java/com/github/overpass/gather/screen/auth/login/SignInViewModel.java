package com.github.overpass.gather.screen.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.data.validator.BaseValidator;
import com.github.overpass.gather.model.repo.login.AuthRepo;
import com.github.overpass.gather.model.usecase.login.SignInUseCase;
import com.google.firebase.auth.FirebaseAuth;

public class SignInViewModel extends ViewModel {

    private final SignInUseCase signInUseCase;

    public SignInViewModel() {
        signInUseCase = new SignInUseCase(new AuthRepo(FirebaseAuth.getInstance()), new BaseValidator());
    }

    public LiveData<SignInStatus> signIn(String email, String password) {
        return signInUseCase.signIn(email, password);
    }

}
