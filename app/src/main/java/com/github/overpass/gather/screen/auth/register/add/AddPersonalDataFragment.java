package com.github.overpass.gather.screen.auth.register.add;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.screen.auth.register.RegisterViewModel;
import com.github.overpass.gather.screen.base.personal.PersonalDataFragment;

import butterknife.OnClick;

public class AddPersonalDataFragment extends PersonalDataFragment<AddPersonalDataViewModel> {

    @Override
    protected AddPersonalDataViewModel createViewModel() {
        ImageSourceUseCase imageSourceUseCase = ViewModelProviders.of(getActivity())
                .get(RegisterViewModel.class)
                .getImageSourceUseCase();
        AddPersonalDataViewModel viewModel = ViewModelProviders.of(this)
                .get(AddPersonalDataViewModel.class);
        viewModel.setImageSourceUseCase(imageSourceUseCase);
        return viewModel;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_add_personal_data;
    }

    @OnClick(R.id.tvSubmit)
    @Override
    public void onSubmitClick() {
        super.onSubmitClick();
    }

    public static AddPersonalDataFragment newInstance() {
        return new AddPersonalDataFragment();
    }
}
