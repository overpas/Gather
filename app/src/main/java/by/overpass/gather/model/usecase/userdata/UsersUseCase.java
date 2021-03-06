package by.overpass.gather.model.usecase.userdata;

import androidx.lifecycle.LiveData;

import by.overpass.gather.data.repo.user.UserDataRepo;
import by.overpass.gather.ui.meeting.chat.users.model.Acceptance;
import by.overpass.gather.ui.meeting.chat.users.model.LoadUsersStatus;

import javax.inject.Inject;

public class UsersUseCase {

    private final UserDataRepo userDataRepo;

    @Inject
    public UsersUseCase(UserDataRepo userDataRepo) {
        this.userDataRepo = userDataRepo;
    }

    public LiveData<LoadUsersStatus> getMembers(String meetingId) {
        return userDataRepo.getMembers(meetingId);
    }

    public LiveData<LoadUsersStatus> getPendingUsers(String meetingId) {
        return userDataRepo.getPendingUsers(meetingId);
    }

    public LiveData<Acceptance> accept(String meetingId, String userId) {
        return userDataRepo.accept(meetingId, userId);
    }
}
