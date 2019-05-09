package com.github.overpass.gather.screen.dialog.delete;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.repo.message.MessageRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.model.usecase.message.MessagesUseCase;
import com.github.overpass.gather.screen.meeting.chat.DeleteStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

public class DeleteMessageViewModel extends ViewModel {

    private final MessagesUseCase messagesUseCase;

    public DeleteMessageViewModel() {
        this.messagesUseCase = new MessagesUseCase(
                new MessageRepo(FirebaseFirestore.getInstance()),
                new UserAuthRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
        );
    }

    public LiveData<DeleteStatus> delete(String meetingId, String messageId) {
        return messagesUseCase.delete(meetingId, Collections.singletonList(messageId));
    }
}
