package com.github.overpass.gather.auth.register.confirm;

import com.github.overpass.gather.SingleLiveEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfirmEmailRepo {

    private final FirebaseAuth firebaseAuth;

    public ConfirmEmailRepo(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public void confirmEmail(SingleLiveEvent<ConfirmEmailStatus> confirmEmailData) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Throwable throwable = new Throwable("Something went wrong");
            confirmEmailData.setValue(new ConfirmEmailStatus.Error(throwable));
            return;
        }
        firebaseUser.reload()
                .addOnSuccessListener(result -> {
                    isEmailVerified(confirmEmailData);
                })
                .addOnFailureListener(e -> {
                    confirmEmailData.setValue(new ConfirmEmailStatus.Error(e));
                });
    }

    public void isEmailVerified(SingleLiveEvent<ConfirmEmailStatus> confirmEmailData) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            confirmEmailData.setValue(new ConfirmEmailStatus.Success());
        } else {
            Throwable throwable = new Throwable("Sorry, your email hasn't been verified");
            confirmEmailData.setValue(new ConfirmEmailStatus.Error(throwable));
        }
    }
}
