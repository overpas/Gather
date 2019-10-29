package com.github.overpass.gather.model.repo.user;

import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.annimon.stream.Stream;
import com.github.overpass.gather.commons.android.lifecycle.LiveDataUtils;
import com.github.overpass.gather.commons.concurrency.Runners;
import com.github.overpass.gather.model.repo.meeting.MeetingsMetadata;
import com.github.overpass.gather.ui.map.AuthUser;
import com.github.overpass.gather.ui.meeting.chat.users.model.Acceptance;
import com.github.overpass.gather.ui.meeting.chat.users.model.LoadUsersStatus;
import com.github.overpass.gather.ui.meeting.chat.users.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UserDataRepo implements MeetingsMetadata, UsersData {

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    @Inject
    public UserDataRepo(FirebaseAuth auth, FirebaseFirestore firestore) {
        this.auth = auth;
        this.firestore = firestore;
    }

    public void getCurrentUserData(Consumer<FirebaseUser> onSuccess, Runnable onError) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            onSuccess.accept(user);
        } else {
            onError.run();
        }
    }

    public LiveData<LoadUsersStatus> getMembers(String meetingId) {
        return getUsers(meetingId, MeetingsMetadata.Users.COLLECTION,
                LoadUsersStatus.MembersSuccess::new);
    }

    public LiveData<LoadUsersStatus> getPendingUsers(String meetingId) {
        return getUsers(meetingId, MeetingsMetadata.PendingUsers.COLLECTION,
                LoadUsersStatus.PendingSuccess::new);
    }

    public LiveData<Acceptance> accept(String meetingId, String userId) {
        MutableLiveData<Acceptance> acceptanceData = new MutableLiveData<>();
        acceptanceData.setValue(Acceptance.PROGRESS);
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(PendingUsers.COLLECTION)
                .whereEqualTo(PendingUsers.FIELD_ID, userId)
                .get()
                .onSuccessTask(Runners.io(), querySnapshot -> {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    AuthUser authUser = documentSnapshot.toObject(AuthUser.class);
                    return firestore.collection(COLLECTION_MEETINGS)
                            .document(meetingId)
                            .collection(Users.COLLECTION)
                            .add(authUser);
                })
                .onSuccessTask(Runners.io(), documentReference ->
                        firestore.collection(COLLECTION_MEETINGS)
                                .document(meetingId)
                                .collection(PendingUsers.COLLECTION)
                                .whereEqualTo(PendingUsers.FIELD_ID, userId)
                                .get()
                                .onSuccessTask(Runners.io(), snapshot -> {
                                    String docId = snapshot.getDocuments().get(0).getId();
                                    return firestore.collection(COLLECTION_MEETINGS)
                                            .document(meetingId)
                                            .collection(PendingUsers.COLLECTION)
                                            .document(docId)
                                            .delete();
                                }))
                .addOnSuccessListener(__ -> {
                    acceptanceData.setValue(Acceptance.SUCCESS);
                })
                .addOnFailureListener(e -> {
                    acceptanceData.setValue(Acceptance.ERROR);
                });
        return acceptanceData;
    }

    private UserModel toUserModel(DocumentSnapshot doc) {
        return new UserModel(
                doc.getId(),
                doc.getString(FIELD_USERNAME),
                doc.getString(FIELD_EMAIL),
                doc.getString(FIELD_PHOTO_URL)
        );
    }

    private LiveData<List<String>> getIds(String meetingId, String subcollection) {
        MutableLiveData<List<String>> idsData = new MutableLiveData<>();
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(subcollection)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> ids = Stream.of(queryDocumentSnapshots.getDocuments())
                            .map(doc -> doc.getString(Users.FIELD_ID))
                            .toList();
                    idsData.setValue(ids);
                })
                .addOnFailureListener(e -> idsData.setValue(null));
        return idsData;
    }

    private LiveData<UserModel> getUser(String id) {
        MutableLiveData<UserModel> idsData = new MutableLiveData<>();
        firestore.collection(COLLECTION_USERS)
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserModel userModel = toUserModel(documentSnapshot);
                    idsData.setValue(userModel);
                })
                .addOnFailureListener(e -> idsData.setValue(null));
        return idsData;
    }

    private LiveData<List<UserModel>> getUsers(List<String> ids) {
        List<LiveData<UserModel>> userModelLiveDatas = new ArrayList<>();
        for (String id : ids) {
            userModelLiveDatas.add(getUser(id));
        }
        return LiveDataUtils.zip(userModelLiveDatas);
    }

    private <T extends LoadUsersStatus.Success> LiveData<LoadUsersStatus> getUsers(
            String meetingId,
            String subcollection,
            Function<List<UserModel>, T> successMapper) {
        MutableLiveData<LoadUsersStatus> usersData = new MutableLiveData<>();
        usersData.setValue(new LoadUsersStatus.Progress());
        Transformations.switchMap(getIds(meetingId, subcollection), this::getUsers)
                .observeForever(userModels -> {
                    usersData.setValue(successMapper.apply(userModels));
                });
        return usersData;
    }
}
