package com.github.overpass.gather.screen.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import butterknife.ButterKnife;

public abstract class BaseFragment<VM extends ViewModel> extends Fragment {

    protected VM viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = createViewModel();
        subscribe();
    }

    protected abstract VM createViewModel();

    protected abstract int getLayoutRes();

    protected void subscribe() {
    }

    public static <F extends Fragment> F newInstance(F fragment, Map<String, String> stringArgs) {
        Bundle arguments = new Bundle();
        for (Map.Entry<String, String> entry : stringArgs.entrySet()) {
            arguments.putString(entry.getKey(), entry.getValue());
        }
        fragment.setArguments(arguments);
        return fragment;
    }
}
