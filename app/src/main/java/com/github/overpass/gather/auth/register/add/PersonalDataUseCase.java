package com.github.overpass.gather.auth.register.add;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.github.overpass.gather.auth.register.UsernameValidator;
import com.google.firebase.auth.FirebaseAuth;

public class PersonalDataUseCase {

    private final UserRepo userRepo;
    private final UploadImageRepo uploadImageRepo;
    private final UsernameValidator validator;
    private final FirebaseAuth firebaseAuth;

    public PersonalDataUseCase(UserRepo userRepo,
                               UploadImageRepo uploadImageRepo,
                               UsernameValidator validator,
                               FirebaseAuth firebaseAuth) {
        this.userRepo = userRepo;
        this.uploadImageRepo = uploadImageRepo;
        this.validator = validator;
        this.firebaseAuth = firebaseAuth;
    }

    private LiveData<AddDataStatus> submit(ContentResolver contentResolver,
                                           String username,
                                           String userId,
                                           @Nullable Uri imageUri) {
        MediatorLiveData<AddDataStatus> addDataStatus = new MediatorLiveData<>();
        uploadImageRepo
                .saveImage(contentResolver, imageUri, UploadImageRepo.BUCKET_AVATARS,
                        userId, "avatar")
                .observeForever(uploadStatus -> {
                    switch (uploadStatus.tag()) {
                        case ImageUploadStatus.ERROR:
                            Throwable throwable = uploadStatus.as(ImageUploadStatus.Error.class)
                                    .getThrowable();
                            addDataStatus.setValue(new AddDataStatus.Error(throwable));
                            break;
                        case ImageUploadStatus.SUCCESS:
                            Uri uri = uploadStatus.as(ImageUploadStatus.Success.class).getUri();
                            userRepo.save(username, uri).observeForever(saveUserStatus -> {
                                switch (saveUserStatus.tag()) {
                                    case SaveUserStatus.ERROR:
                                        addDataStatus.setValue(
                                                new AddDataStatus.Error(saveUserStatus
                                                        .as(SaveUserStatus.Error.class)
                                                        .getThrowable())
                                        );
                                        break;
                                    case SaveUserStatus.SUCCESS:
                                        addDataStatus.setValue(new AddDataStatus.Success());
                                        break;
                                }
                            });
                            break;
                    }
                });
        return addDataStatus;
    }

    private LiveData<AddDataStatus> submit(String username) {
        MediatorLiveData<AddDataStatus> addDataStatus = new MediatorLiveData<>();
        userRepo.save(username, null).observeForever(saveUserStatus -> {
            switch (saveUserStatus.tag()) {
                case SaveUserStatus.ERROR:
                    addDataStatus.setValue(
                            new AddDataStatus.Error(saveUserStatus.as(SaveUserStatus.Error.class)
                                    .getThrowable())
                    );
                    break;
                case SaveUserStatus.SUCCESS:
                    addDataStatus.setValue(new AddDataStatus.Success());
                    break;
            }
        });
        return addDataStatus;
    }

    LiveData<AddDataStatus> submit(ContentResolver resolver,
                                   String username,
                                   @Nullable Uri imageUri) {
        MediatorLiveData<AddDataStatus> addDataStatus = new MediatorLiveData<>();
        addDataStatus.setValue(new AddDataStatus.Progress());
        if (!validator.isUsernameValid(username)) {
            addDataStatus.setValue(new AddDataStatus.InvalidUsername());
            return addDataStatus;
        }
        if (firebaseAuth.getCurrentUser() == null) {
            addDataStatus.setValue(new AddDataStatus.Error(new Throwable("User not found")));
            return addDataStatus;
        }
        String userId = firebaseAuth.getCurrentUser().getUid();
        return imageUri == null ? submit(username) : submit(resolver, username, userId, imageUri);
    }
}
