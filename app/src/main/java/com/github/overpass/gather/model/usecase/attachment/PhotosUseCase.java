package com.github.overpass.gather.model.usecase.attachment;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.commons.LiveDataUtils;
import com.github.overpass.gather.model.commons.exception.PhotoUploadException;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.upload.UploadImageRepo;
import com.github.overpass.gather.screen.auth.register.add.ImageUploadStatus;
import com.github.overpass.gather.screen.map.Meeting;
import com.github.overpass.gather.screen.meeting.chat.attachments.PhotoUploadStatus;

import java.util.UUID;

public class PhotosUseCase {

    private final MeetingRepo meetingRepo;
    private final UploadImageRepo uploadImageRepo;

    public PhotosUseCase(MeetingRepo meetingRepo,
                         UploadImageRepo uploadImageRepo) {
        this.meetingRepo = meetingRepo;
        this.uploadImageRepo = uploadImageRepo;
    }

    public LiveData<Meeting> getMeeting(String meetingId) {
        return meetingRepo.getLiveMeeting(meetingId);
    }


    public LiveData<PhotoUploadStatus> loadPhoto(ContentResolver contentResolver,
                                                 @NonNull Uri imageUri,
                                                 String meetingId) {
        return Transformations.switchMap(
                uploadImageRepo.saveImage(contentResolver, imageUri,
                        UploadImageRepo.BUCKET_MEETINGS, meetingId, UUID.randomUUID().toString()),
                status -> {
                    if (status instanceof ImageUploadStatus.Success) {
                        Uri uri = status.as(ImageUploadStatus.Success.class).getUri();
                        return Transformations.map(
                                meetingRepo.addPhoto(meetingId, uri.toString()),
                                photoAdded -> {
                                    if (photoAdded) {
                                        return new PhotoUploadStatus.Success();
                                    } else {
                                        return new PhotoUploadStatus.Error(new PhotoUploadException());
                                    }
                                }
                        );
                    } else if (status instanceof ImageUploadStatus.Error) {
                        Throwable throwable = status.as(ImageUploadStatus.Error.class).getThrowable();
                        return LiveDataUtils.just(new PhotoUploadStatus.Error(throwable));
                    } else {
                        return LiveDataUtils.just(new PhotoUploadStatus.Progress());
                    }
                }
        );
    }
}
