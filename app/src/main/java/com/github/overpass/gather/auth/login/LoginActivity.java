package com.github.overpass.gather.auth.login;

import android.os.Bundle;

import com.github.overpass.gather.FragmentUtils;
import com.github.overpass.gather.R;
import com.github.overpass.gather.auth.login.SignInFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
