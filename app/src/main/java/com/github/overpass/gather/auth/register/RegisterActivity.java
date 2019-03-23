package com.github.overpass.gather.auth.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ViewFlipper;

import com.github.overpass.gather.R;

public class RegisterActivity extends AppCompatActivity implements RegistrationController {

    private RegisterViewModel viewModel;

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        subscribe();
    }

    private void subscribe() {
        viewModel.getRegistrationProgressData().observe(this, this::moveToNextStep);
    }

    private void moveToNextStep(Integer step) {
        if (viewModel.shouldShowNextStep(step)) {
            viewFlipper.showNext();
        } else {

        }
    }

    @OnClick(R.id.tvNext)
    public void onNext() {
        viewModel.next();
    }

    @Override
    public void moveToNextStep() {
        viewModel.next();
    }
}
