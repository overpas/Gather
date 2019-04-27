package com.github.overpass.gather.screen.auth.login;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.overpass.gather.model.commons.FragmentUtils;
import com.github.overpass.gather.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        if (savedInstanceState == null) {
            FragmentUtils.replace(getSupportFragmentManager(), R.id.flAuthContainer,
                    SignInFragment.newInstance(), false);
        }
    }
}
