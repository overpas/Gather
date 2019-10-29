package com.github.overpass.gather.model.usecase.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.ui.map.AuthUser;

import javax.inject.Inject;

public class AllowedUseCase {

    private final MeetingRepo meetingRepo;
    private final UserAuthRepo userAuthRepo;

    @Inject
    public AllowedUseCase(MeetingRepo meetingRepo, UserAuthRepo userAuthRepo) {
        this.meetingRepo = meetingRepo;
        this.userAuthRepo = userAuthRepo;
    }

    public LiveData<Boolean> isAllowed(String meetingId) {
        return Transformations.switchMap(
                userAuthRepo.getCurrentUser(AuthUser.Role.USER),
                user -> meetingRepo.isUserAllowed(user, meetingId)
        );
    }
}
