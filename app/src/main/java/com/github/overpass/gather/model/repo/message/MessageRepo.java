package com.github.overpass.gather.model.repo.message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.annimon.stream.Stream;
import com.github.overpass.gather.model.repo.meeting.MeetingsData;
import com.github.overpass.gather.model.repo.user.UserData;
import com.github.overpass.gather.screen.meeting.chat.MessageStatus;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class MessageRepo implements MeetingsData {

    private final FirebaseFirestore firestore;

    public MessageRepo(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public LiveData<MessageStatus> messages(String meetingId) {
        MutableLiveData<MessageStatus> messagesData = new MutableLiveData<>();
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(Messages.COLLECTION)
                .orderBy(Messages.FIELD_DATE, Query.Direction.DESCENDING)
                .addSnapshotListener(new MessageMapper() {
                    @Override
                    void onSuccess(MessageStatus.Success success) {
                        messagesData.setValue(success);
                    }

                    @Override
                    void onError(MessageStatus.Error error) {
                        messagesData.setValue(error);
                    }
                });
        return messagesData;
    }

    public void send(String meetingId, @NonNull String input, @NonNull UserData userData) {
        firestore.collection(COLLECTION_MEETINGS)
                .document(meetingId)
                .collection(Messages.COLLECTION)
                .add(new BaseMessage(
                        input,
                        new Date(),
                        userData.getId(),
                        userData.getName(),
                        userData.getPhotoUrl()
                ));
    }

    private static abstract class MessageMapper implements EventListener<QuerySnapshot> {

        @Override
        public void onEvent(@Nullable QuerySnapshot snapshots,
                            @Nullable FirebaseFirestoreException e) {
            if (e != null) {
                onError(new MessageStatus.Error(e));
            }
            if (snapshots != null) {
                List<Message> messages = Stream.of(snapshots.getDocuments())
                        .map(this::toMessage)
                        .toList();
                onSuccess(new MessageStatus.Success(messages));
            }
        }

        abstract void onSuccess(MessageStatus.Success success);

        abstract void onError(MessageStatus.Error error);

        private Message toMessage(DocumentSnapshot documentSnapshot) {
            return new Message(
                    documentSnapshot.getId(),
                    documentSnapshot.getString(Messages.FIELD_TEXT),
                    documentSnapshot.getDate(Messages.FIELD_DATE),
                    documentSnapshot.getString(Messages.FIELD_AUTHOR_ID),
                    documentSnapshot.getString(Messages.FIELD_AUTHOR_NAME),
                    documentSnapshot.getString(Messages.FIELD_AUTHOR_PHOTO_URL)
            );
        }
    }
}