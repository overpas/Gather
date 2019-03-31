package com.github.overpass.gather.create;


import android.app.Activity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.CancellableTask;
import com.google.firebase.storage.OnProgressListener;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WithUserTask extends CancellableTask<DocumentReference> {

    private final FirebaseUser user;
    private final DocumentReference documentReference;

    public WithUserTask(FirebaseAuth firebaseAuth, DocumentReference documentReference) {
        user = firebaseAuth.getCurrentUser();
        this.documentReference = documentReference;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public boolean isSuccessful() {
        return user != null;
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public boolean isInProgress() {
        return false;
    }

    @Override
    public CancellableTask<DocumentReference> addOnProgressListener(@NonNull OnProgressListener<? super DocumentReference> onProgressListener) {
        return this;
    }

    @Override
    public CancellableTask<DocumentReference> addOnProgressListener(@NonNull Executor executor, @NonNull OnProgressListener<? super DocumentReference> onProgressListener) {
        return this;
    }

    @Override
    public CancellableTask<DocumentReference> addOnProgressListener(@NonNull Activity activity, @NonNull OnProgressListener<? super DocumentReference> onProgressListener) {
        return this;
    }

    @Nullable
    @Override
    public DocumentReference getResult() {
        return documentReference;
    }

    @Nullable
    @Override
    public <X extends Throwable> DocumentReference getResult(@NonNull Class<X> aClass) {
        return null;
    }

    @Nullable
    @Override
    public Exception getException() {
        return new IllegalStateException("You are not authorized!");
    }

    @NonNull
    @Override
    public Task<DocumentReference> addOnSuccessListener(@NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<DocumentReference> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<DocumentReference> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<DocumentReference> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<DocumentReference> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<DocumentReference> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        return this;
    }

    public FirebaseUser getUser() {
        return user;
    }
}
