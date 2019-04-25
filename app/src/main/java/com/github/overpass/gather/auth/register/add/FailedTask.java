package com.github.overpass.gather.auth.register.add;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

public class FailedTask<T> extends Task<T> {

    private final String message;

    public FailedTask(String message) {
        this.message = message;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Nullable
    @Override
    public T getResult() {
        return null;
    }

    @Nullable
    @Override
    public <X extends Throwable> T getResult(@NonNull Class<X> aClass) throws X {
        return null;
    }

    @Nullable
    @Override
    public Exception getException() {
        return new UploadFailedException(message);
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull OnSuccessListener<? super T> onSuccessListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull Executor executor,
                                        @NonNull OnSuccessListener<? super T> onSuccessListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<T> addOnSuccessListener(@NonNull Activity activity,
                                        @NonNull OnSuccessListener<? super T> onSuccessListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull Executor executor,
                                        @NonNull OnFailureListener onFailureListener) {
        return this;
    }

    @NonNull
    @Override
    public Task<T> addOnFailureListener(@NonNull Activity activity,
                                        @NonNull OnFailureListener onFailureListener) {
        return this;
    }

    public static class UploadFailedException extends Exception {
        public UploadFailedException(String message) {
            super(message);
        }
    }
}
