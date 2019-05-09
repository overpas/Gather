package com.github.overpass.gather.screen.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.github.overpass.gather.model.repo.meeting.MeetingRepo;
import com.github.overpass.gather.model.repo.meeting.MeetingWithId;
import com.github.overpass.gather.model.usecase.search.SearchUseCase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private final SearchUseCase searchUseCase;

    private MediatorLiveData<List<MeetingWithId>> meetingData = new MediatorLiveData<>();

    public SearchViewModel() {
        this.searchUseCase = new SearchUseCase(new MeetingRepo(FirebaseFirestore.getInstance()));
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
