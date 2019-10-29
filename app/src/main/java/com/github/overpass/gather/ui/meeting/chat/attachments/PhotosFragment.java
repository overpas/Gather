package com.github.overpass.gather.ui.meeting.chat.attachments;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.overpass.gather.App;
import com.github.overpass.gather.R;
import com.github.overpass.gather.ui.base.personal.DataFragment;
import com.github.overpass.gather.ui.dialog.progress.determinate.ProgressPercentDialogFragment;
import com.github.overpass.gather.ui.map.Meeting;
import com.github.overpass.gather.ui.meeting.chat.attachments.closeup.CloseupActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.github.overpass.gather.commons.android.SnackbarKt.snackbar;

public class PhotosFragment extends DataFragment<PhotosViewModel> {

    @BindView(R.id.rvAttachments)
    RecyclerView rvAttachments;
    @BindView(R.id.ivPhotoPreview)
    ImageView ivPhotoPreview;
    @BindView(R.id.fabAttach)
    FloatingActionButton fabAttach;

    private PhotosAdapter adapter;

    @NotNull
    @Override
    protected PhotosViewModel createViewModel() {
        return getViewModelProvider().get(PhotosViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_photos;
    }

    @Override
    protected void inject() {
        App.Companion.getComponentManager(this)
                .getAttachmentsDetailsComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupList();
        getViewModel().getMeeting().observe(getViewLifecycleOwner(), this::handleMeeting);
        getViewModel().getSuggestToChooseData().observe(getViewLifecycleOwner(), this::handleChoose);
        getViewModel().photoUploadSuccess().observe(getViewLifecycleOwner(), v -> handleUploadSuccess());
        getViewModel().photoUploadProgress().observe(getViewLifecycleOwner(), this::handleUploadProgress);
        getViewModel().photoUploadError().observe(getViewLifecycleOwner(), this::handleUploadError);
    }

    private void handleUploadProgress(int percent) {
        ProgressPercentDialogFragment.progress(requireFragmentManager(), percent);
    }

    private void handleUploadError(String message) {
        ProgressPercentDialogFragment.hide(requireFragmentManager());
        snackbar(ivPhotoPreview, message, Snackbar.LENGTH_SHORT);
    }

    private void handleUploadSuccess() {
        ProgressPercentDialogFragment.hide(requireFragmentManager());
        snackbar(ivPhotoPreview, getString(R.string.success), Snackbar.LENGTH_SHORT);
        getViewModel().resetChosenImage();
    }

    private void handleChoose(Boolean shouldSuggestToChoose) {
        if (shouldSuggestToChoose != null && shouldSuggestToChoose) {
            super.onChooseImageClick();
        }
    }

    @OnClick(R.id.fabAttach)
    public void doAction() {
        getViewModel().doAction();
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
            CloseupActivity.start(requireContext(), photoUrl);
        });
        rvAttachments.setAdapter(adapter);
    }

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }
}
