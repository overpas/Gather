package com.github.overpass.gather.screen.meeting.enrolled;

import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.LottieAnimationView;
import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class EnrolledActivity extends BaseActivity<EnrolledViewModel> {

    @BindView(R.id.lavLargeTick)
    LottieAnimationView lavLargeTick;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_enrolled;
    }

    @Override
    protected EnrolledViewModel createViewModel() {
        return ViewModelProviders.of(this).get(EnrolledViewModel.class);
    }

    @Override
    protected void onBind() {
        if (viewModel.shouldAnimate()) {
            lavLargeTick.playAnimation();
        }
    }

    @OnClick(R.id.btnOk)
    public void onOkClick() {
        finish();
    }
}
