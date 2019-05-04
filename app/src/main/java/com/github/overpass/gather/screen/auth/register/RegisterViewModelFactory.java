package com.github.overpass.gather.screen.auth.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.ConcurrentHashMap;

public class RegisterViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static final ConcurrentHashMap<Integer, RegisterViewModelFactory> pool
            = new ConcurrentHashMap<>();

    private final int initialStep;

    private RegisterViewModelFactory(int initialStep) {
        this.initialStep = initialStep;
    }

    @NonNull
    public static RegisterViewModelFactory getInstance(int initialStep) {
        RegisterViewModelFactory factory = pool.get(initialStep);
        if (factory == null) {
            factory = new RegisterViewModelFactory(initialStep);
            pool.put(initialStep, factory);
        }
        return factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RegisterViewModel(initialStep);
    }
}
