package com.github.overpass.gather.screen.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.repo.meeting.MeetingWithId;
import com.github.overpass.gather.screen.meeting.MeetingActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchBottomFragment extends BottomSheetDialogFragment {

    private static final String TAG = "SearchBottomFragment";

    @BindView(R.id.svSearch)
    SearchView svSearch;
    @BindView(R.id.rvMeetings)
    RecyclerView rvMeetings;

    private SearchViewModel viewModel;
    private MeetingsAdapter meetingsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMeetings.setLayoutManager(new LinearLayoutManager(getContext()));
        meetingsAdapter = new MeetingsAdapter(id -> {
            MeetingActivity.start(getContext(), id);
        });
        rvMeetings.setAdapter(meetingsAdapter);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.queryResults().observe(getViewLifecycleOwner(), this::handleMeetings);
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    viewModel.setQueryText(newText);
                }
                return false;
            }
        });
    }

    private void handleMeetings(List<MeetingWithId> meetingWithIds) {
        meetingsAdapter.setItems(meetingWithIds);
    }

    public static void open(FragmentManager fragmentManager) {
        SearchBottomFragment fragment = new SearchBottomFragment();
        fragment.show(fragmentManager, TAG);
    }
}
