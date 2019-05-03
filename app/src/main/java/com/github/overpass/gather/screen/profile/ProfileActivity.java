package com.github.overpass.gather.screen.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.FragmentUtils;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        setupActionBar();
        if (savedInstanceState == null) {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flProfileContainer,
                    ProfileFragment.newInstance(), false);
        }
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
