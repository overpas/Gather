package com.github.overpass.gather.screen.meeting.chat.attachments;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.github.overpass.gather.App;
import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.base.personal.DataFragment;
import com.github.overpass.gather.screen.dialog.progress.determinate.ProgressPercentDialogFragment;
import com.github.overpass.gather.screen.map.Meeting;
import com.github.overpass.gather.screen.meeting.chat.attachments.closeup.CloseupActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

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
        App.Companion.getAppComponentManager(this)
                .getAttachmentsDetailsComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupList();
        getViewModel().getMeeting(getMeetingId()).observe(getViewLifecycleOwner(), this::handleMeeting);
        getViewModel().getSuggestToChooseData().observe(getViewLifecycleOwner(), this::handleChoose);
        getViewModel().photoUploadSuccess().observe(getViewLifecycleOwner(), v -> handleUploadSuccess());
        getViewModel().photoUploadProgress().observe(getViewLifecycleOwner(), this::handleUploadProgress);
        getViewModel().photoUploadError().observe(getViewLifecycleOwner(), this::handleUploadError);
    }

    @Override
    protected void clearComponent() {
        super.clearComponent();
        App.Companion.getAppComponentManager(this)
                .clearAttachmentDetailsComponent();
    }

    private void handleUploadProgress(int percent) {
        ProgressPercentDialogFragment.progress(requireFragmentManager(), percent);
    }

    private void handleUploadError(String message) {
        ProgressPercentDialogFragment.hide(requireFragmentManager());
        snackbar(ivPhotoPreview, message);
    }

    private void handleUploadSuccess() {
        ProgressPercentDialogFragment.hide(requireFragmentManager());
        snackbar(ivPhotoPreview, getString(R.string.success));
        getViewModel().resetChosenImage();
    }

    private void handleChoose(Boolean shouldSuggestToChoose) {
        if (shouldSuggestToChoose != null && shouldSuggestToChoose) {
            super.onChooseImageClick();
        }
    }

    @OnClick(R.id.fabAttach)
    public void doAction() {
        getViewModel().doAction(getMeetingId());
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
