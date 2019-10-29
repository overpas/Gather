package com.github.overpass.gather.commons.android.lifecycle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class LiveDataUtils {

    public static <T> LiveData<List<T>> zip(List<LiveData<T>> liveDatas) {
        MediatorLiveData<List<T>> mediatorLiveData = new MediatorLiveData<>();
        if (liveDatas.isEmpty()) {
            mediatorLiveData.setValue(null);
        }
        Map<LiveData<T>, Integer> liveDataVersions = new HashMap<>();
        List<T> buffer = new ArrayList<>();
        for (LiveData<T> liveData: liveDatas) {
            liveDataVersions.put(liveData, 0);
        }
        for (LiveData<T> liveData: liveDatas) {
            mediatorLiveData.addSource(liveData, t -> {
                Integer version = liveDataVersions.get(liveData);
                liveDataVersions.put(liveData, ++version);
                buffer.add(t);
                if (areSameVersions(liveDataVersions) && allStarted(liveDataVersions, liveDatas)) {
                    mediatorLiveData.setValue(buffer);
                    buffer.clear();
                }
            });
        }
        return mediatorLiveData;
    }

    private static <T> boolean allStarted(Map<LiveData<T>, Integer> liveDataVersions, List<LiveData<T>> liveDatas) {
        return liveDataVersions.size() == liveDatas.size();
    }

    private static <T> boolean areSameVersions(Map<LiveData<T>, Integer> liveDataVersions) {
        HashSet<Integer> set = new HashSet<>();
        for (Map.Entry<LiveData<T>, Integer> entry : liveDataVersions.entrySet()) {
            set.add(entry.getValue());
        }
        return set.size() < 2;
    }

    public static <T> LiveData<T> just(T t) {
        MutableLiveData<T> data = new MutableLiveData<>();
        data.setValue(t);
        return data;
    }
}
