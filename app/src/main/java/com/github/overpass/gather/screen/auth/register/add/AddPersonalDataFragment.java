package com.github.overpass.gather.screen.auth.register.add;

import android.content.Intent;

import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.screen.auth.register.RegisterViewModel;
import com.github.overpass.gather.screen.auth.register.RegisterViewModelFactory;
import com.github.overpass.gather.screen.base.personal.DataFragment;
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment;
import com.github.overpass.gather.screen.map.MapActivity;

import butterknife.OnClick;

public class AddPersonalDataFragment extends DataFragment<AddPersonalDataViewModel> {

    @Override
    protected AddPersonalDataViewModel createViewModel() {
        RegisterViewModelFactory factory = RegisterViewModelFactory.getInstance(getInitialStep());
        ImageSourceUseCase imageSourceUseCase = ViewModelProviders.of(getActivity(), factory)
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

    @OnClick(R.id.ivPhotoPreview)
    @Override
    protected void onChooseImageClick() {
        super.onChooseImageClick();
    }

    @Override
    protected void handleSuccess(AddDataStatus.Success success) {
        super.handleSuccess(success);
        getViewModel().setSignUpComplete();
        ProgressDialogFragment.Companion.hide(getFragmentManager());
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public static AddPersonalDataFragment newInstance() {
        return new AddPersonalDataFragment();
    }
}
