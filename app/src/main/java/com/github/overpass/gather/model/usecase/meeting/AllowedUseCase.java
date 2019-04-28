package com.github.overpass.gather.model.usecase.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.user.UserRepo;
import com.github.overpass.gather.screen.map.AuthUser;

public class AllowedUseCase {

    private final MeetingRepo meetingRepo;
    private final UserRepo userRepo;

    public AllowedUseCase(MeetingRepo meetingRepo, UserRepo userRepo) {
        this.meetingRepo = meetingRepo;
        this.userRepo = userRepo;
    }

    public LiveData<Boolean> isAllowed(String meetingId) {
        return Transformations.switchMap(
                userRepo.getCurrentUser(AuthUser.Role.USER),
                user -> meetingRepo.isUserAllowed(user, meetingId)
        );
    }
}
