package com.github.overpass.gather.model.repo.confirm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.overpass.gather.auth.register.confirm.ConfirmEmailStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfirmEmailRepo {

    private final FirebaseAuth firebaseAuth;

    public ConfirmEmailRepo(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<ConfirmEmailStatus> confirmEmail() {
        MutableLiveData<ConfirmEmailStatus> confirmEmailStatus = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Throwable throwable = new Throwable("Something went wrong");
            confirmEmailStatus.setValue(new ConfirmEmailStatus.Error(throwable));
            return confirmEmailStatus;
        }
        firebaseUser.reload()
                .addOnSuccessListener(result -> {
                    confirmEmailStatus.setValue(isEmailVerified());
                })
                .addOnFailureListener(e -> {
                    confirmEmailStatus.setValue(new ConfirmEmailStatus.Error(e));
                });
        return confirmEmailStatus;
    }

    private ConfirmEmailStatus isEmailVerified() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            return new ConfirmEmailStatus.Success();
        } else {
            Throwable throwable = new Throwable("Sorry, your email hasn't been verified");
            return new ConfirmEmailStatus.Error(throwable);
        }
    }
}
