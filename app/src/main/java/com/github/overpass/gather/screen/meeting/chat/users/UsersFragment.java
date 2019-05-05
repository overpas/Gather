package com.github.overpass.gather.screen.meeting.chat.users;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.base.BaseFragment;

import java.util.Collections;

import butterknife.BindView;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;

public class UsersFragment extends BaseFragment<UsersViewModel> {

    public static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    @BindView(R.id.rvMembers)
    RecyclerView rvMembers;
    @BindView(R.id.rvPendingUsers)
    RecyclerView rvPendingUsers;

    private UsersAdapter membersAdapter;
    private UsersAdapter pendingUsersAdapter;

    @Override
    protected UsersViewModel createViewModel() {
        return ViewModelProviders.of(this).get(UsersViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_users;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupList();
    }

    @Override
    protected void subscribe() {
        super.subscribe();
        viewModel.getMembers(getMeetingId()).observe(getViewLifecycleOwner(), this::handleUsers);
        viewModel.getPendingUsers(getMeetingId())
                .observe(getViewLifecycleOwner(), this::handleUsers);
    }

    private void handleUsers(LoadUsersStatus status) {
        switch (status.tag()) {
            case LoadUsersStatus.ERROR:
                handleError(status.as(LoadUsersStatus.Error.class));
                break;
            case LoadUsersStatus.PROGRESS:
                handleProgress(status.as(LoadUsersStatus.Progress.class));
                break;
            case LoadUsersStatus.SUCCESS:
                handleSuccess(status.as(LoadUsersStatus.Success.class));
                break;
        }
    }

    private void handleError(LoadUsersStatus.Error error) {
        snackbar(rvMembers, error.getThrowable().getLocalizedMessage());
    }

    private void handleProgress(LoadUsersStatus.Progress progress) {

    }

    private void handleSuccess(LoadUsersStatus.Success success) {
        if (success.tag().equals(LoadUsersStatus.MEMBERS_SUCCESS)) {
            membersAdapter.setUsers(success.getMembers());
        } else {
            pendingUsersAdapter.setUsers(success.getMembers());
        }
    }

    private void setupList() {
        membersAdapter = new UsersAdapter((id) -> {});
        rvMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMembers.setAdapter(membersAdapter);
        pendingUsersAdapter = new UsersAdapter((id) -> {});
        rvPendingUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPendingUsers.setAdapter(pendingUsersAdapter);
    }

    @NonNull
    private String getMeetingId() {
        Bundle arguments = getArguments();
        String meetingId = "-1";
        if (arguments != null)  {
            meetingId = arguments.getString(MEETING_ID_KEY, "-1");
        }
        return meetingId;
    }

    public static UsersFragment newInstance(String id) {
        return newInstance(new UsersFragment(), Collections.singletonMap(MEETING_ID_KEY, id));
    }
}
