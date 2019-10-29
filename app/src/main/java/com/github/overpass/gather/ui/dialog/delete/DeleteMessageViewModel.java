package com.github.overpass.gather.ui.dialog.delete;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.usecase.message.MessagesUseCase;
import com.github.overpass.gather.ui.meeting.chat.DeleteStatus;

import java.util.Collections;

import javax.inject.Inject;

public class DeleteMessageViewModel extends ViewModel {

    private final MessagesUseCase messagesUseCase;
    private final String meetingId;

    @Inject
    public DeleteMessageViewModel(MessagesUseCase messagesUseCase, String meetingId) {
        this.messagesUseCase = messagesUseCase;
        this.meetingId = meetingId;
    }

    public LiveData<DeleteStatus> delete(String messageId) {
        return messagesUseCase.delete(meetingId, Collections.singletonList(messageId));
    }
}
