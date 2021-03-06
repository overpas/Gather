package by.overpass.gather.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import by.overpass.gather.data.repo.meeting.MeetingWithId;
import by.overpass.gather.model.usecase.search.SearchUseCase;

import java.util.List;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private final SearchUseCase searchUseCase;

    private MediatorLiveData<List<MeetingWithId>> meetingData = new MediatorLiveData<>();

    @Inject
    public SearchViewModel(SearchUseCase searchUseCase) {
        this.searchUseCase = searchUseCase;
    }

    public void setQueryText(String newText) {
        meetingData.addSource(searchUseCase.searchByName(newText), meetings -> {
            meetingData.setValue(meetings);
        });
    }

    public LiveData<List<MeetingWithId>> queryResults() {
        return meetingData;
    }
}
