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
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.meeting.chat.users.list.PendingUsersAdapter;
import com.github.overpass.gather.screen.meeting.chat.users.list.UsersAdapter;
import com.github.overpass.gather.screen.meeting.chat.users.model.Acceptance;
import com.github.overpass.gather.screen.meeting.chat.users.model.LoadUsersStatus;

import java.util.Collections;

import butterknife.BindView;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;
import static com.github.overpass.gather.model.commons.UIUtil.toast;

public class UsersFragment extends BaseFragment<UsersViewModel> {

    public static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    @BindView(R.id.rvMembers)
    RecyclerView rvMembers;
    @BindView(R.id.rvPendingUsers)
    RecyclerView rvPendingUsers;

    private UsersAdapter membersAdapter;
    private PendingUsersAdapter pendingUsersAdapter;

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
    protected void onBind() {
        super.onBind();
        getViewModel().getMembers(getMeetingId()).observe(getViewLifecycleOwner(), this::handleUsers);
        getViewModel().getPendingUsers(getMeetingId())
                .observe(getViewLifecycleOwner(), this::handleUsers);
        getViewModel().checkUserRole(getMeetingId()).observe(getViewLifecycleOwner(), this::handleRole);
    }

    private void handleRole(AuthUser.Role role) {
        if (role == AuthUser.Role.USER) {
            pendingUsersAdapter.setSwipeLocked(true);
        } else if (role == AuthUser.Role.NOBODY) {
            toast(this, getString(R.string.not_allowed));
            getActivity().finish();
        }
    }

    private void handleUsers(LoadUsersStatus status) {
        switch (status.tag()) {
            case LoadUsersStatus.ERROR:
                handleError(status.as(LoadUsersStatus.Error.class));
                break;
            case LoadUsersStatus.PROGRESS:
                handleProgress(status.as(LoadUsersStatus.Progress.class));
                break;
            case LoadUsersStatus.MEMBERS_SUCCESS:
                handleMembersSuccess(status.as(LoadUsersStatus.MembersSuccess.class));
                break;
            case LoadUsersStatus.PENDING_SUCCESS:
                handlePendingUsersSuccess(status.as(LoadUsersStatus.PendingSuccess.class));
                break;
        }
    }

    private void handleError(LoadUsersStatus.Error error) {
        snackbar(rvMembers, error.getThrowable().getLocalizedMessage());
    }

    private void handleProgress(LoadUsersStatus.Progress progress) {
        membersAdapter.setProgress();
        pendingUsersAdapter.setProgress();
    }

    private void handleMembersSuccess(LoadUsersStatus.MembersSuccess success) {
        membersAdapter.setUsers(success.getUsers());
    }

    private void handlePendingUsersSuccess(LoadUsersStatus.PendingSuccess success) {
        pendingUsersAdapter.setUsers(success.getUsers());
    }

    private void setupList() {
        membersAdapter = new UsersAdapter((id) -> {
        });
        rvMembers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMembers.setAdapter(membersAdapter);
        pendingUsersAdapter = new PendingUsersAdapter(this::handleAcceptPendingUser);
        rvPendingUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPendingUsers.setAdapter(pendingUsersAdapter);
    }

    private void handleAcceptPendingUser(String id) {
        getViewModel().acceptUser(getMeetingId(), id)
                .observe(getViewLifecycleOwner(), this::handleAcceptance);
    }

    private void handleAcceptance(Acceptance acceptance) {
        switch (acceptance) {
            case ERROR:
                handleAcceptanceError();
                break;
            case SUCCESS:
                handleAcceptanceSuccess();
                break;
            case PROGRESS:
                handleAcceptanceProgress();
                break;
        }
    }

    private void handleAcceptanceProgress() {
        ProgressDialogFragment.show(getFragmentManager());
    }

    private void handleAcceptanceSuccess() {
        ProgressDialogFragment.hide(getFragmentManager());
        getActivity().recreate();
    }

    private void handleAcceptanceError() {
        ProgressDialogFragment.hide(getFragmentManager());
        snackbar(rvMembers, getString(R.string.couldnt_accept));
    }

    @NonNull
    private String getMeetingId() {
        Bundle arguments = getArguments();
        String meetingId = "-1";
        if (arguments != null) {
            meetingId = arguments.getString(MEETING_ID_KEY, "-1");
        }
        return meetingId;
    }

    public static UsersFragment newInstance(String id) {
        return Companion.newInstance(new UsersFragment(), Collections.singletonMap(MEETING_ID_KEY, id));
    }
}
