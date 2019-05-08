package com.github.overpass.gather.model.usecase.meeting;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.commons.exception.SubscriptionException;
import com.github.overpass.gather.model.repo.meeting.JoinRepo;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.subscription.SubscriptionRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.meeting.MeetingAndRatio;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.github.overpass.gather.screen.meeting.join.JoinStatus;
import com.github.overpass.gather.screen.meeting.join.LoadPrivateMeetingStatus;

public class MeetingUseCase {

    private final MeetingRepo meetingRepo;
    private final JoinRepo joinRepo;
    private final UserAuthRepo userAuthRepo;
    private final SubscriptionRepo subscriptionRepo;

    public MeetingUseCase(MeetingRepo meetingRepo,
                          JoinRepo joinRepo,
                          UserAuthRepo userAuthRepo,
                          SubscriptionRepo subscriptionRepo) {
        this.meetingRepo = meetingRepo;
        this.joinRepo = joinRepo;
        this.userAuthRepo = userAuthRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    public LiveData<LoadMeetingStatus> loadMeeting(String meetingId) {
        return meetingRepo.getFullMeeting(meetingId);
    }

    public LiveData<JoinStatus> join(String meetingId) {
        return Transformations.switchMap(meetingRepo.getMeeting(meetingId), meeting -> {
            if (meeting.isPrivate()) {
                final AuthUser[] authUser = new AuthUser[1];
                return Transformations.switchMap(
                        joinAsUser(user -> {
                            authUser[0] = user;
                            return joinRepo.enrollPrivate(meetingId, user);
                        }),
                        status -> Transformations.map(
                                subscriptionRepo.subscribeToAccepted(authUser[0], meetingId),
                                subscribed -> {
                                    if (subscribed) {
                                        return status;
                                    } else {
                                        return new JoinStatus.Error(new SubscriptionException());
                                    }
                                }
                        )
                );
            } else {
                return joinAsUser(user -> joinRepo.joinPublic(meetingId, user));
            }
        });
    }

    private LiveData<JoinStatus> joinAsUser(Function<AuthUser, LiveData<JoinStatus>> joining) {
        return Transformations.switchMap(userAuthRepo.getCurrentUser(AuthUser.Role.USER), joining);
    }

    public LiveData<LoadPrivateMeetingStatus> loadMeetingCheckEnrolled(String meetingId) {
        return Transformations.switchMap(
                userAuthRepo.getCurrentUser(AuthUser.Role.USER),
                user -> Transformations.switchMap(
                        meetingRepo.isAlreadyEnrolled(user, meetingId),
                        isEnrolled -> Transformations.map(
                                meetingRepo.getFullMeeting(meetingId),
                                status -> {
                                    LoadPrivateMeetingStatus result;
                                    switch (status.tag()) {
                                        case LoadMeetingStatus.ERROR:
                                            result = new LoadPrivateMeetingStatus.Error();
                                            break;
                                        case LoadMeetingStatus.SUCCESS:
                                            MeetingAndRatio meetingAndRatio
                                                    = status.as(LoadMeetingStatus.Success.class)
                                                    .getMeetingAndRatio();
                                            result = new LoadPrivateMeetingStatus.Success(
                                                    meetingAndRatio,
                                                    isEnrolled
                                            );
                                            break;
                                        default:
                                        case LoadMeetingStatus.PROGRESS:
                                            result = new LoadPrivateMeetingStatus.Progress();
                                    }
                                    return result;
                                }
                        )
                )
        );
    }
}
