package com.github.overpass.gather.model.usecase.meeting;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.repo.meeting.JoinRepo;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.github.overpass.gather.screen.meeting.join.JoinStatus;

public class MeetingUseCase {

    private final MeetingRepo meetingRepo;
    private final JoinRepo joinRepo;
    private final UserAuthRepo userAuthRepo;

    public MeetingUseCase(MeetingRepo meetingRepo, JoinRepo joinRepo, UserAuthRepo userAuthRepo) {
        this.meetingRepo = meetingRepo;
        this.joinRepo = joinRepo;
        this.userAuthRepo = userAuthRepo;
    }

    public LiveData<LoadMeetingStatus> loadMeeting(String meetingId) {
        return meetingRepo.getFullMeeting(meetingId);
    }

    public LiveData<JoinStatus> join(String meetingId) {
        return Transformations.switchMap(meetingRepo.getMeeting(meetingId), meeting -> {
            if (meeting.isPrivate()) {
                return joinAsUser(user -> joinRepo.enrollPrivate(meetingId, user));
            } else {
                return joinAsUser(user -> joinRepo.joinPublic(meetingId, user));
            }
        });
    }

    private LiveData<JoinStatus> joinAsUser(Function<AuthUser, LiveData<JoinStatus>> joining) {
        return Transformations.switchMap(userAuthRepo.getCurrentUser(AuthUser.Role.USER), joining);
    }
}
