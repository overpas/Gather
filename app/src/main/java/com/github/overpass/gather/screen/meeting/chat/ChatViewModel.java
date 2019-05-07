package com.github.overpass.gather.screen.meeting.chat;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.annimon.stream.Stream;
import com.github.overpass.gather.model.repo.message.Message;
import com.github.overpass.gather.model.repo.message.MessageRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.model.usecase.message.MessagesUseCase;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatViewModel extends BaseMeetingViewModel {

    private final MessagesUseCase messagesUseCase;

    public ChatViewModel() {
        this.messagesUseCase = new MessagesUseCase(
                new MessageRepo(FirebaseFirestore.getInstance()),
                new UserAuthRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance()));
    }

    public LiveData<MessageModel> messages(String messageId) {
        return Transformations.map(messagesUseCase.messages(messageId), status -> {
            if (status.tag().equals(MessageStatus.ERROR)) {
                return new MessageModel.Error(status.as(MessageStatus.Error.class));
            } else {
                List<Message> messages = status.as(MessageStatus.Success.class).getMessages();
                return new MessageModel.Success(mapMessages(messages));
            }
        });
    }

    private List<IMessageImpl> mapMessages(List<Message> messages) {
        return Stream.of(messages)
                .map(message -> new IMessageImpl(
                        message.getId(),
                        message.getText(),
                        new IUserImpl(
                                message.getAuthorId(),
                                message.getAuthorName(),
                                message.getAuthorPhotoUrl()
                        ),
                        message.getDate()
                ))
                .toList();
    }

    public LiveData<AuthUser> getCurrentUser() {
        return messagesUseCase.getCurrentUser();
    }

    public void send(String meetingId, String input) {
        messagesUseCase.send(meetingId, input);
    }
}
