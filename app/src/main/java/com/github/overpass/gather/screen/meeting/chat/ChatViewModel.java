package com.github.overpass.gather.screen.meeting.chat;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.annimon.stream.Stream;
import com.github.overpass.gather.model.repo.message.Message;
import com.github.overpass.gather.model.usecase.meeting.MeetingUseCase;
import com.github.overpass.gather.model.usecase.message.MessagesUseCase;
import com.github.overpass.gather.model.usecase.userdata.RoleUseCase;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingViewModel;

import java.util.List;

import javax.inject.Inject;

public class ChatViewModel extends BaseMeetingViewModel {

    private final MessagesUseCase messagesUseCase;
    private final RoleUseCase roleUseCase;
    private final MutableLiveData<Integer> selectedItemsData;

    @Inject
    public ChatViewModel(MeetingUseCase meetingUseCase,
                         RoleUseCase roleUseCase,
                         MessagesUseCase messagesUseCase,
                         MutableLiveData<Integer> selectedItemsData) {
        super(meetingUseCase);
        this.roleUseCase = roleUseCase;
        this.messagesUseCase = messagesUseCase;
        this.selectedItemsData = selectedItemsData;
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

    public LiveData<AuthUser.Role> checkUserRole(String meetingId) {
        return roleUseCase.getCurrentUserRole(meetingId);
    }

    public void setSelection(int count) {
        selectedItemsData.setValue(count);
    }

    public LiveData<Integer> selectedItems() {
        return selectedItemsData;
    }

    public LiveData<DeleteStatus> delete(String meetingId, List<String> ids) {
        return messagesUseCase.delete(meetingId, ids);
    }
}
