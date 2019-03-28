package com.github.overpass.gather.create;

import android.os.Bundle;

import com.github.overpass.gather.FragmentUtils;
import com.github.overpass.gather.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewMeetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        if (savedInstanceState == null) {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flNewMeetingContainer,
                    NewMeetingFragment.newInstance(), false);
        }
    }
}
