package com.github.overpass.gather.auth.register;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.github.overpass.gather.R;
import com.github.overpass.gather.base.BaseActivity;
import com.github.overpass.gather.dialog.PickImageDialogFragment;

import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;

public class RegisterActivity extends BaseActivity<RegisterViewModel>
        implements RegistrationController, PickImageDialogFragment.OnClickListener {

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setViewFlipperAnimations();
    }

    private void setViewFlipperAnimations() {
        Animation imgAnimationIn = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        imgAnimationIn.setDuration(700);
        viewFlipper.setInAnimation(imgAnimationIn);
        Animation imgAnimationOut = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        imgAnimationOut.setDuration(700);
        viewFlipper.setOutAnimation(imgAnimationOut);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected RegisterViewModel createViewModel() {
        return ViewModelProviders.of(this).get(RegisterViewModel.class);
    }

    @Override
    protected void subscribe() {
        viewModel.getRegistrationProgressData().observe(this, this::moveToNextStep);
    }

    private void moveToNextStep(Integer step) {
        if (viewModel.shouldShowNextStep(step)) {
            viewFlipper.showNext();
        }
    }

    @Override
    public void moveToNextStep() {
        viewModel.next();
    }

    @Override
    public void onGallery() {
        viewModel.onGallery();
    }

    @Override
    public void onCamera() {
        viewModel.onCamera();
    }
}
