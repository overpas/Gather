package com.github.overpass.gather.screen.meeting.chat.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.FragmentUtils;

public class UsersActivity extends AppCompatActivity {

    private static final String MEETING_ID_KEY = "MEETING_ID_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setupToolbar();
        if (savedInstanceState == null && getIntent().getExtras() != null) {
            String meetingId = getIntent().getExtras().getString(MEETING_ID_KEY);
            showUsersFragment(meetingId);
        }
    }

    private void showUsersFragment(@Nullable String meetingId) {
        if (meetingId != null) {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flUsersContainer,
                    UsersFragment.newInstance(meetingId), false);
        }
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.members);
        }
    }

    public static void start(Context context, String meetingId) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(MEETING_ID_KEY, meetingId);
        context.startActivity(intent);
    }
}
