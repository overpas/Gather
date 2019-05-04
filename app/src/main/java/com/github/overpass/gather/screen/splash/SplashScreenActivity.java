package com.github.overpass.gather.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.auth.login.LoginActivity;
import com.github.overpass.gather.screen.auth.register.RegisterActivity;
import com.github.overpass.gather.screen.auth.register.RegisterViewModel;
import com.github.overpass.gather.screen.base.BaseActivity;
import com.github.overpass.gather.screen.map.MapActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends BaseActivity<SplashViewModel> {

    @BindView(R.id.ivLogo)
    ImageView ivLogo;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected SplashViewModel createViewModel() {
        return ViewModelProviders.of(this).get(SplashViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            playAnimation();
        }
    }

    private void playAnimation() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new LinearInterpolator());
        fadeIn.setDuration(1300);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(SplashScreenActivity.this::proceed, 700);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        ivLogo.startAnimation(fadeIn);
    }

    private void proceed() {
        viewModel.onSplashAnimationComplete().observe(this, this::handleStartStatus);
    }

    private void handleStartStatus(StartStatus startStatus) {
        switch (startStatus) {
            case AUTHORIZED:
                handleAuthorized();
                break;
            case UNAUTHORIZED:
                handleUnauthorized();
                break;
            case NOT_ADDED_DATA:
                handleNotAddedData();
                break;
            case UNCONFIRMED_EMAIL:
                handleUnconfirmedEmail();
                break;
        }
    }

    private void handleUnconfirmedEmail() {
        RegisterActivity.start(this, 1);
    }

    private void handleNotAddedData() {
        RegisterActivity.start(this, 2);
    }

    private void handleUnauthorized() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void handleAuthorized() {
        startActivity(new Intent(this, MapActivity.class));
        finish();
    }
}
