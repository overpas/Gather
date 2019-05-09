package com.github.overpass.gather.model.usecase.message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.repo.message.MessageRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.meeting.chat.MessageStatus;

public class MessagesUseCase {

    private final MessageRepo messageRepo;
    private final UserAuthRepo userAuthRepo;

    public MessagesUseCase(MessageRepo messageRepo, UserAuthRepo userAuthRepo) {
        this.messageRepo = messageRepo;
        this.userAuthRepo = userAuthRepo;
    }

    public LiveData<MessageStatus> messages(String meetingId) {
        return messageRepo.messages(meetingId);
    }

    public LiveData<AuthUser> getCurrentUser() {
        return userAuthRepo.getCurrentUser(AuthUser.Role.USER);
    }

    public void send(String meetingId, String input) {
        userAuthRepo.getCurrentUser().observeForever(userData -> {
            if (userData != null) {
                messageRepo.send(meetingId, input, userData);
            }
        });
    }
}