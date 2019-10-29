package com.github.overpass.gather.model.usecase.userdata;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.data.validator.Validator;
import com.github.overpass.gather.model.repo.upload.UploadImageRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.screen.auth.register.add.AddDataStatus;
import com.github.overpass.gather.screen.auth.register.add.ImageUploadStatus;
import com.github.overpass.gather.screen.auth.register.add.SaveUserStatus;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

public class PersonalDataUseCase {

    private final UserAuthRepo userAuthRepo;
    private final UploadImageRepo uploadImageRepo;
    private final Validator<String> validator;
    private final FirebaseAuth firebaseAuth;

    @Inject
    public PersonalDataUseCase(UserAuthRepo userAuthRepo,
                               UploadImageRepo uploadImageRepo,
                               Validator<String> validator,
                               FirebaseAuth firebaseAuth) {
        this.userAuthRepo = userAuthRepo;
        this.uploadImageRepo = uploadImageRepo;
        this.validator = validator;
        this.firebaseAuth = firebaseAuth;
    }

    private LiveData<AddDataStatus> submit(ContentResolver contentResolver,
                                           String username,
                                           String userId,
                                           @Nullable Uri imageUri) {
        MediatorLiveData<AddDataStatus> addDataStatus = new MediatorLiveData<>();
        addDataStatus.setValue(new AddDataStatus.Progress());
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
                            userAuthRepo.save(username, uri).observeForever(saveUserStatus -> {
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
        return Transformations.map(userAuthRepo.save(username, null), status -> {
            AddDataStatus addDataStatus;
            switch (status.tag()) {
                case SaveUserStatus.ERROR:
                    Throwable throwable = status.as(SaveUserStatus.Error.class).getThrowable();
                    addDataStatus = new AddDataStatus.Error(throwable);
                    break;
                case SaveUserStatus.PROGRESS:
                    addDataStatus = new AddDataStatus.Progress();
                    break;
                case SaveUserStatus.SUCCESS:
                default:
                    addDataStatus = new AddDataStatus.Success();
            }
            return addDataStatus;
        });
    }

    public LiveData<AddDataStatus> submit(ContentResolver resolver,
                                          String username,
                                          @Nullable Uri imageUri) {
        MediatorLiveData<AddDataStatus> addDataStatus = new MediatorLiveData<>();
        addDataStatus.setValue(new AddDataStatus.Progress());
        if (!validator.isValid(username)) {
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
