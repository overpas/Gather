package com.github.overpass.gather.model.commons.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import butterknife.ButterKnife;

public abstract class BaseActivity<VM extends ViewModel> extends AppCompatActivity {

    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        viewModel = createViewModel();
        subscribe();
    }

    protected abstract int getLayoutRes();

    protected abstract VM createViewModel();

    protected void subscribe() {
    }
}
