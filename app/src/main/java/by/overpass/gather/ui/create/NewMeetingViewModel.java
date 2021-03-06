package by.overpass.gather.ui.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import by.overpass.gather.model.usecase.meeting.CreateMeetingUseCase;
import by.overpass.gather.ui.map.SaveMeetingStatus;

import java.util.Date;

import javax.inject.Inject;

public class NewMeetingViewModel extends ViewModel {

    private final CreateMeetingUseCase createMeetingUseCase;

    @Inject
    public NewMeetingViewModel(CreateMeetingUseCase createMeetingUseCase) {
        this.createMeetingUseCase = createMeetingUseCase;
    }

    public LiveData<SaveMeetingStatus> createMeeting(double latitude,
                                                     double longitude,
                                                     String title,
                                                     Date date,
                                                     MeetingType type,
                                                     int maxPeople,
                                                     boolean isPrivate) {
        return createMeetingUseCase.createMeeting(latitude, longitude, title, date, type,
                maxPeople, isPrivate);
    }
}
