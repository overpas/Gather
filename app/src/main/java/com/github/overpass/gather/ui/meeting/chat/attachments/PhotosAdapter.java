package com.github.overpass.gather.ui.meeting.chat.attachments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.overpass.gather.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.AttachmentViewHolder> {

    private final List<String> photos = new ArrayList<>();
    private final OnClickListener listener;

    public PhotosAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttachmentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int position) {
        holder.bind(photos.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setItems(@Nullable List<String> newPhotos) {
        if (newPhotos != null && !newPhotos.isEmpty()) {
            photos.clear();
            photos.addAll(newPhotos);
            notifyDataSetChanged();
        }
    }

    public class AttachmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPhotoAttachment)
        ImageView ivPhotoAttachment;

        AttachmentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String photoUrl, OnClickListener listener) {
            Glide.with(itemView)
                    .load(photoUrl)
                    .into(ivPhotoAttachment);
            itemView.setOnClickListener((view) -> listener.onItemClick(photoUrl));
        }
    }

    public interface OnClickListener {
        void onItemClick(String photoUrl);
    }
}
