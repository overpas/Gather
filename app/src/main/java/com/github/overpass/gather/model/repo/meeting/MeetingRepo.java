package com.github.overpass.gather.model.repo.meeting;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.annimon.stream.Stream;
import com.github.overpass.gather.model.commons.Runners;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.map.Meeting;
import com.github.overpass.gather.screen.map.SaveMeetingStatus;
import com.github.overpass.gather.model.data.FailedTask;
import com.github.overpass.gather.screen.create.MeetingType;
import com.github.overpass.gather.screen.map.detail.Current2MaxPeopleRatio;
import com.github.overpass.gather.screen.meeting.MeetingAndRatio;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.annimon.stream.Objects.nonNull;

public class MeetingRepo implements MeetingsData {

    private static final String TAG = "MeetingRepo";

    private final FirebaseFirestore firestore;
    private MediatorLiveData<LoadMeetingStatus> meetingStatus = new MediatorLiveData<>();

    public MeetingRepo(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public LiveData<Map<String, Meeting>> getMeetings(double latitude,
                                                      double longitude,
                                                      double radius) {
        MutableLiveData<Map<String, Meeting>> meetingData = new MutableLiveData<>();
        firestore.collection(COLLECTION_MEETINGS)
                .whereGreaterThanOrEqualTo(FIELD_LATITUDE, latitude - radius)
                .whereLessThanOrEqualTo(FIELD_LATITUDE, latitude + radius)
//              .whereGreaterThanOrEqualTo(FIELD_LONGITUDE, longitude - radius)
//              .whereLessThanOrEqualTo(FIELD_LONGITUDE, longitude + radius)
//              FUCK YOU FIREBASE
                .get()
                .addOnSuccessListener(docs -> {
                    Map<String, Meeting> map = Stream.of(docs.getDocuments())
                            .custom(documentSnapshotStream -> {
                                Map<String, Meeting> meetingMap = new HashMap<>();
                                documentSnapshotStream.forEach(documentSnapshot -> {
                                    meetingMap.put(documentSnapshot.getId(),
                                            documentSnapshot.toObject(Meeting.class));
                                });
                                return meetingMap;
                            });
                    meetingData.setValue(map);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                });
        return meetingData;
    }

    public LiveData<SaveMeetingStatus> save(double latitude,
                                            double longitude,
                                            Date date,
                                            String name,
                                            MeetingType type,
                                            int maxPeople,
                                            boolean isPrivate,
                                            @Nullable AuthUser authUser) {
        MutableLiveData<SaveMeetingStatus> resultData = new MutableLiveData<>();
        resultData.setValue(new SaveMeetingStatus.Progress());
        final String[] meetingId = new String[1];
        firestore.collection(COLLECTION_MEETINGS)
                .add(new Meeting(name, latitude, longitude, date, type.getType(), maxPeople,
                        isPrivate))
                .addOnFailureListener(e -> {
                    resultData.setValue(new SaveMeetingStatus.Error(e));
                })
                .onSuccessTask(Runners.io(), docRef -> {
                    if (authUser != null) {
                        meetingId[0] = docRef.getId();
                        return docRef.collection(MeetingsData.Users.COLLECTION)
                                .add(authUser);
                    }
                    return new FailedTask<>("Something Went Wrong!");
                })
                .addOnSuccessListener(result -> {
                    resultData.setValue(new SaveMeetingStatus.Success(meetingId[0]));
                })
                .addOnFailureListener(e -> {
                    resultData.setValue(new SaveMeetingStatus.Error(e));
                });
        return resultData;
    }

    public LiveData<Current2MaxPeopleRatio> getCurrent2MaxRatio(String id) {
        MutableLiveData<Current2MaxPeopleRatio> maxPeopleData = new MutableLiveData<>();
        final int[] numbers = new int[2];
        firestore.collection(COLLECTION_MEETINGS)
                .document(id)
                .get()
                .onSuccessTask(Runners.io(), docRef -> {
                    numbers[0] = (int) docRef.getLong("maxPeople").longValue();
                    return docRef.getReference()
                            .collection(MeetingsData.Users.COLLECTION)
                            .get();
                })
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    numbers[1] = queryDocumentSnapshots.size();
                    maxPeopleData.setValue(new Current2MaxPeopleRatio(numbers[1], numbers[0]));
                })
                .addOnFailureListener(e -> {
                    maxPeopleData.setValue(Current2MaxPeopleRatio.FAILED);
                });
        return maxPeopleData;
    }

    public LiveData<LoadMeetingStatus> getFullMeeting(String meetingId) {
        if (meetingStatus.getValue() != null
                && meetingStatus.getValue() instanceof LoadMeetingStatus.Success) {
            return meetingStatus;
        }
        meetingStatus.setValue(new LoadMeetingStatus.Progress());
        LiveData<MeetingAndRatio> meetingAndRatioData = Transformations.switchMap(
                getMeeting(meetingId),
                meeting -> Transformations.switchMap(getCurrent2MaxRatio(meetingId), ratio -> {
                    MutableLiveData<MeetingAndRatio> resultData = new MutableLiveData<>();
                    resultData.setValue(new MeetingAndRatio(meeting, ratio));
                    return resultData;
                })
        );
        meetingStatus.addSource(meetingAndRatioData, meetingAndRatio -> {
            if (!nonNull(meetingAndRatio) || meetingAndRatio.getMeeting().equals(Meeting.EMPTY)
                    || meetingAndRatio.getRatio().equals(Current2MaxPeopleRatio.FAILED)) {
                meetingStatus.setValue(new LoadMeetingStatus.Error());
            } else {
                meetingStatus.setValue(new LoadMeetingStatus.Success(meetingAndRatio));
            }
        });
        return meetingStatus;
    }

    public LiveData<Meeting> getMeeting(String meetingId) {
        MutableLiveData<Meeting> meetingStatus = new MutableLiveData<>();
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .get()
                .addOnSuccessListener(doc -> {
                    meetingStatus.setValue(doc.toObject(Meeting.class));
                })
                .addOnFailureListener(e -> meetingStatus.setValue(Meeting.EMPTY));
        return meetingStatus;
    }

    public LiveData<Meeting> getLiveMeeting(String meetingId) {
        MutableLiveData<Meeting> meetingStatus = new MutableLiveData<>();
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .addSnapshotListener((doc, e) -> {
                    if (doc != null && e == null) {
                        Meeting meeting = doc.toObject(Meeting.class);
                        meetingStatus.setValue(meeting);
                    }
                });
        return meetingStatus;
    }

    public LiveData<Boolean> isUserAllowed(@Nullable AuthUser authUser, String meetingId) {
        return isUserListed(Users.COLLECTION, authUser, meetingId);
    }

    public LiveData<Boolean> isAlreadyEnrolled(@Nullable AuthUser authUser, String meetingId) {
        return isUserListed(PendingUsers.COLLECTION, authUser, meetingId);
    }

    public LiveData<Boolean> isUserListed(String subcollection,
                                          @Nullable AuthUser authUser,
                                          String meetingId) {
        MutableLiveData<Boolean> isAllowedData = new MutableLiveData<>();
        if (authUser == null) {
            isAllowedData.setValue(false);
            return isAllowedData;
        }
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(subcollection)
                .whereEqualTo(Users.FIELD_ID, authUser.getId())
                .get()
                .addOnSuccessListener(doc -> {
                    isAllowedData.setValue(!doc.isEmpty());
                })
                .addOnFailureListener(e -> {
                    isAllowedData.setValue(false);
                });
        return isAllowedData;
    }

    public LiveData<List<MeetingWithId>> searchByName(String text) {
        MutableLiveData<List<MeetingWithId>> meetingData = new MutableLiveData<>();
        firestore.collection(COLLECTION_MEETINGS)
                .whereEqualTo(FIELD_NAME, text)
                .addSnapshotListener((docs, e) -> {
                    if (e != null || docs == null || docs.isEmpty()) {
                        meetingData.setValue(null);
                    } else {
                        List<MeetingWithId> meetings = Stream.of(docs.getDocuments())
                                .map(doc -> new MeetingWithId(
                                                doc.toObject(Meeting.class),
                                                doc.getId()
                                        )
                                )
                                .toList();
                        meetingData.setValue(meetings);
                    }
                });
        return meetingData;
    }

    public LiveData<Boolean> addPhoto(String meetingId, String photoUrl) {
        MutableLiveData<Boolean> photoAddedData = new MutableLiveData<>();
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .get()
                .onSuccessTask(Runners.io(), doc -> {
                    if (doc == null) {
                        return new FailedTask<>("Something went wrong");
                    } else {
                        Meeting meeting = doc.toObject(Meeting.class);
                        List<String> photos = meeting.getPhotos();
                        photos.add(photoUrl);
                        return firestore.collection(COLLECTION_MEETINGS)
                                .document(meetingId)
                                .update(MeetingsData.FIELD_PHOTOS, photos);
                    }
                })
                .addOnSuccessListener(__ -> photoAddedData.setValue(true))
                .addOnFailureListener(e -> photoAddedData.setValue(false));
        return photoAddedData;
    }
}
