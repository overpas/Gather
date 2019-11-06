package by.overpass.gather.ui.meeting.chat;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.annimon.stream.Stream;
import by.overpass.gather.data.repo.message.Message;
import by.overpass.gather.model.usecase.meeting.MeetingUseCase;
import by.overpass.gather.model.usecase.message.MessagesUseCase;
import by.overpass.gather.model.usecase.userdata.RoleUseCase;
import by.overpass.gather.ui.map.AuthUser;
import by.overpass.gather.ui.meeting.base.LoadMeetingStatus;

import java.util.List;

import javax.inject.Inject;

public class ChatViewModel extends ViewModel {

    private final MeetingUseCase meetingUseCase;
    private final MessagesUseCase messagesUseCase;
    private final RoleUseCase roleUseCase;
    private final MutableLiveData<Integer> selectedItemsData;
    private final String meetingId;

    @Inject
    public ChatViewModel(MeetingUseCase meetingUseCase,
                         RoleUseCase roleUseCase,
                         MessagesUseCase messagesUseCase,
                         MutableLiveData<Integer> selectedItemsData,
                         String meetingId) {
        this.meetingUseCase = meetingUseCase;
        this.roleUseCase = roleUseCase;
        this.messagesUseCase = messagesUseCase;
        this.selectedItemsData = selectedItemsData;
        this.meetingId = meetingId;
    }

    public LiveData<MessageModel> messages() {
        return Transformations.map(messagesUseCase.messages(meetingId), status -> {
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

    public void send(String input) {
        messagesUseCase.send(meetingId, input);
    }

    public LiveData<AuthUser.Role> checkUserRole() {
        return roleUseCase.getCurrentUserRole(meetingId);
    }

    public void setSelection(int count) {
        selectedItemsData.setValue(count);
    }

    public LiveData<Integer> selectedItems() {
        return selectedItemsData;
    }

    public LiveData<DeleteStatus> delete(List<String> ids) {
        return messagesUseCase.delete(meetingId, ids);
    }

    public LiveData<LoadMeetingStatus> loadMeeting() {
        return meetingUseCase.loadMeeting(meetingId);
    }
}
