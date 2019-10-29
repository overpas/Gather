package com.github.overpass.gather.model.usecase.meeting;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.commons.exception.FailedToSubscribe;
import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.subscription.SubscriptionRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.screen.create.MeetingType;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.map.SaveMeetingStatus;

import java.util.Date;

import javax.inject.Inject;

public class CreateMeetingUseCase {

    private final MeetingRepo meetingRepo;
    private final UserAuthRepo userAuthRepo;
    private final SubscriptionRepo subscriptionRepo;

    @Inject
    public CreateMeetingUseCase(MeetingRepo meetingRepo, UserAuthRepo userAuthRepo, SubscriptionRepo subscriptionRepo) {
        this.meetingRepo = meetingRepo;
        this.userAuthRepo = userAuthRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    public LiveData<SaveMeetingStatus> createMeeting(double latitude,
                                                     double longitude,
                                                     String title,
                                                     Date date,
                                                     MeetingType type,
                                                     int maxPeople,
                                                     boolean isPrivate) {
        if (TextUtils.isEmpty(title)) {
            MutableLiveData<SaveMeetingStatus> resultData = new MutableLiveData<>();
            resultData.setValue(new SaveMeetingStatus.EmptyName());
            return resultData;
        }
        return Transformations.switchMap(
                userAuthRepo.getCurrentUser(AuthUser.Role.ADMIN),
                user -> subscribeToPendingUsers(
                        meetingRepo.save(latitude, longitude, date, title, type, maxPeople,
                                isPrivate, user)
                )
        );
    }

    private LiveData<SaveMeetingStatus> subscribeToPendingUsers(LiveData<SaveMeetingStatus> data) {
        return Transformations.switchMap(data, saveStatus -> {
            MutableLiveData<SaveMeetingStatus> resultData = new MutableLiveData<>();
            resultData.setValue(new SaveMeetingStatus.Progress());
            if (saveStatus.tag().equals(SaveMeetingStatus.SUCCESS)) {
                SaveMeetingStatus.Success success = saveStatus.as(SaveMeetingStatus.Success.class);
                subscriptionRepo.subscribeToPendingUsers(success.getMeetingId())
                        .observeForever(subscribed -> {
                            if (!subscribed) {
                                resultData.setValue(new SaveMeetingStatus.Error(
                                        new FailedToSubscribe()));
                            } else {
                                resultData.setValue(saveStatus);
                            }
                        });
            }
            return resultData;
        });
    }
}
