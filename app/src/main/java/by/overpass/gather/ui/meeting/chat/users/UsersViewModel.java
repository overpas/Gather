package by.overpass.gather.ui.meeting.chat.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import by.overpass.gather.model.usecase.userdata.RoleUseCase;
import by.overpass.gather.model.usecase.userdata.UsersUseCase;
import by.overpass.gather.ui.map.AuthUser;
import by.overpass.gather.ui.meeting.chat.users.model.Acceptance;
import by.overpass.gather.ui.meeting.chat.users.model.LoadUsersStatus;

import javax.inject.Inject;

public class UsersViewModel extends ViewModel {

    private final UsersUseCase usersUseCase;
    private final RoleUseCase roleUseCase;
    private final String meetingId;

    @Inject
    public UsersViewModel(UsersUseCase usersUseCase,
                          RoleUseCase roleUseCase,
                          String meetingId) {
        this.roleUseCase = roleUseCase;
        this.usersUseCase = usersUseCase;
        this.meetingId = meetingId;
    }

    public LiveData<LoadUsersStatus> getMembers() {
        return usersUseCase.getMembers(meetingId);
    }

    public LiveData<LoadUsersStatus> getPendingUsers() {
        return usersUseCase.getPendingUsers(meetingId);
    }

    public LiveData<Acceptance> acceptUser(String userId) {
        return usersUseCase.accept(meetingId, userId);
    }

    public LiveData<AuthUser.Role> checkUserRole() {
        return roleUseCase.getCurrentUserRole(meetingId);
    }
}
