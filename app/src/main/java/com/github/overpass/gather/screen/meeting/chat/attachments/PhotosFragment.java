package com.github.overpass.gather.screen.meeting.chat.attachments;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase;
import com.github.overpass.gather.screen.base.personal.DataFragment;
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment;
import com.github.overpass.gather.screen.map.Meeting;
import com.github.overpass.gather.screen.meeting.chat.attachments.closeup.CloseupActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.model.commons.Constants.MEETING_ID_KEY;
import static com.github.overpass.gather.model.commons.UIUtil.snackbar;

public class PhotosFragment extends DataFragment<PhotosViewModel> {

    @BindView(R.id.rvAttachments)
    RecyclerView rvAttachments;
    @BindView(R.id.ivPhotoPreview)
    ImageView ivPhotoPreview;
    @BindView(R.id.fabAttach)
    FloatingActionButton fabAttach;

    private PhotosAdapter adapter;

    @Override
    protected PhotosViewModel createViewModel() {
        ImageSourceUseCase imageSourceUseCase = ViewModelProviders.of(getActivity())
                .get(GeneralPhotoViewModel.class)
                .getImageSourceUseCase();
        PhotosViewModel photosViewModel = ViewModelProviders.of(this)
                .get(PhotosViewModel.class);
        photosViewModel.setImageSourceUseCase(imageSourceUseCase);
        return photosViewModel;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_photos;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupList();
        viewModel.getMeeting(getMeetingId()).observe(getViewLifecycleOwner(), this::handleMeeting);
        viewModel.getSuggestToChooseData().observe(getViewLifecycleOwner(), this::handleChoose);
        viewModel.getPhotoUploadData().observe(getViewLifecycleOwner(), this::handleUpload);
    }

    private void handleUpload(PhotoUploadStatus uploadStatus) {
        switch (uploadStatus.tag()) {
            case PhotoUploadStatus.PROGRESS:
                handleUploadProgress(uploadStatus.as(PhotoUploadStatus.Progress.class));
                break;
            case PhotoUploadStatus.ERROR:
                handleUploadError(uploadStatus.as(PhotoUploadStatus.Error.class));
                break;
            case PhotoUploadStatus.SUCCESS:
                handleUploadSuccess(uploadStatus.as(PhotoUploadStatus.Success.class));
                break;
        }
    }

    private void handleUploadProgress(PhotoUploadStatus.Progress progress) {
        ProgressDialogFragment.show(getFragmentManager());
    }

    private void handleUploadError(PhotoUploadStatus.Error error) {
        ProgressDialogFragment.hide(getFragmentManager());
        snackbar(ivPhotoPreview, error.getThrowable().getLocalizedMessage());
    }

    private void handleUploadSuccess(PhotoUploadStatus.Success success) {
        ProgressDialogFragment.hide(getFragmentManager());
        snackbar(ivPhotoPreview, getString(R.string.success));
        viewModel.resetChosenImage();
    }

    private void handleChoose(Boolean shouldSuggestToChoose) {
        if (shouldSuggestToChoose != null && shouldSuggestToChoose) {
            super.onChooseImageClick();
        }
    }

    @OnClick(R.id.fabAttach)
    public void doAction() {
        viewModel.doAction(getMeetingId());
    }

    @Override
    protected void handleChosenImageUri(@Nullable Uri uri) {
        if (uri != null) {
            ivPhotoPreview.setVisibility(View.VISIBLE);
            ivPhotoPreview.setImageURI(uri);
            fabAttach.setImageResource(R.drawable.ic_send);
        } else {
            ivPhotoPreview.setVisibility(View.GONE);
            fabAttach.setImageResource(R.drawable.ic_attach);
        }
    }

    private void handleMeeting(Meeting meeting) {
        List<String> photos = meeting.getPhotos();
        adapter.setItems(photos);
    }

    private void setupList() {
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                RecyclerView.VERTICAL);
        rvAttachments.setLayoutManager(layoutManager);
        adapter = new PhotosAdapter(photoUrl -> {
            CloseupActivity.start(getContext(), photoUrl);
        });
        rvAttachments.setAdapter(adapter);
    }

    protected String getMeetingId() {
        String defaultId = "-1";
        if (getArguments() != null) {
            String id = getArguments().getString(MEETING_ID_KEY, defaultId);
            if (id != null) {
                return id;
            }
        }
        return defaultId;
    }

    public static PhotosFragment newInstance(String meetingId) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(MEETING_ID_KEY, meetingId);
        fragment.setArguments(args);
        return fragment;
    }
}
