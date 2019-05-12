package com.github.overpass.gather.screen.meeting.chat.attachments;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.github.overpass.gather.model.commons.SingleLiveEvent;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.upload.UploadImageRepo;
import com.github.overpass.gather.model.usecase.attachment.PhotosUseCase;
import com.github.overpass.gather.screen.base.personal.DataViewModel;
import com.github.overpass.gather.screen.map.Meeting;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class PhotosViewModel extends DataViewModel {

    private final PhotosUseCase attachmentsUseCase;
    private final MediatorLiveData<PhotoUploadStatus> photoUploadData;
    private final SingleLiveEvent<Boolean> suggestToChooseData;

    public PhotosViewModel(@NonNull Application application) {
        super(application);
        attachmentsUseCase = new PhotosUseCase(
                new MeetingRepo(FirebaseFirestore.getInstance()),
                new UploadImageRepo(FirebaseStorage.getInstance())
        );
        photoUploadData = new MediatorLiveData<>();
        suggestToChooseData = new SingleLiveEvent<>();
    }

    public LiveData<PhotoUploadStatus> getPhotoUploadData() {
        return photoUploadData;
    }

    public LiveData<Boolean> getSuggestToChooseData() {
        return suggestToChooseData;
    }

    public LiveData<Meeting> getMeeting(String meetingId) {
        return attachmentsUseCase.getMeeting(meetingId);
    }

    public void doAction(String meetingId) {
        Uri imageUri = getSelectedUri();
        if (imageUri != null) {
            ContentResolver contentResolver = getApplication().getContentResolver();
            final boolean[] progressStarted = new boolean[1];
            photoUploadData.addSource(
                    attachmentsUseCase.loadPhoto(contentResolver, imageUri, meetingId),
                    photoUploadStatus -> {
                        if (photoUploadStatus instanceof PhotoUploadStatus.Progress) {
                            if (!progressStarted[0]) {
                                photoUploadData.setValue(photoUploadStatus);
                            }
                            progressStarted[0] = true;
                        } else {
                            photoUploadData.setValue(photoUploadStatus);
                        }
                    }
            );
        } else {
            suggestToChooseData.setValue(true);
        }
    }
}
