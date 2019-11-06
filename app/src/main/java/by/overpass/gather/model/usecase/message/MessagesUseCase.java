package by.overpass.gather.model.usecase.message;

import androidx.lifecycle.LiveData;

import by.overpass.gather.data.repo.message.MessageRepo;
import by.overpass.gather.data.repo.user.UserAuthRepo;
import by.overpass.gather.ui.map.AuthUser;
import by.overpass.gather.ui.meeting.chat.DeleteStatus;
import by.overpass.gather.ui.meeting.chat.MessageStatus;

import java.util.List;

import javax.inject.Inject;

public class MessagesUseCase {

    private final MessageRepo messageRepo;
    private final UserAuthRepo userAuthRepo;

    @Inject
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

    public LiveData<DeleteStatus> delete(String meetingId, List<String> ids) {
        return messageRepo.delete(meetingId, ids);
    }
}
