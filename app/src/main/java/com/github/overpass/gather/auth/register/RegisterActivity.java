package com.github.overpass.gather.auth.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.WindowManager;

import com.github.overpass.gather.R;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel viewModel;

    @BindView(R.id.viewPager)
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        viewPager.setAdapter(viewModel.getAdapter(getSupportFragmentManager()));
        subscribe();
    }

    private void subscribe() {
        viewModel.getRegistrationProgressData().observe(this, this::moveToNextStep);
    }

    @OnClick(R.id.tvNext)
    public void onNext() {
        viewModel.next();
    }

    public void moveToNextStep(int nextItem) {
        viewPager.setCurrentItem(nextItem);
    }

}
