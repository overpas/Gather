package com.github.overpass.gather.screen.meeting.chat.users;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.repo.user.UserDataRepo;
import com.github.overpass.gather.model.usecase.userdata.UsersUseCase;
import com.github.overpass.gather.screen.meeting.chat.users.model.Acceptance;
import com.github.overpass.gather.screen.meeting.chat.users.model.LoadUsersStatus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsersViewModel extends ViewModel {

    private final UsersUseCase usersUseCase;

    public UsersViewModel() {
        this.usersUseCase = new UsersUseCase(
                new UserDataRepo(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
        );
    }

    public LiveData<LoadUsersStatus> getMembers(String meetingId) {
        return usersUseCase.getMembers(meetingId);
    }

    public LiveData<LoadUsersStatus> getPendingUsers(String meetingId) {
        return usersUseCase.getPendingUsers(meetingId);
    }

    public LiveData<Acceptance> acceptUser(String meetingId, String userId) {
        return usersUseCase.accept(meetingId, userId);
    }
}
