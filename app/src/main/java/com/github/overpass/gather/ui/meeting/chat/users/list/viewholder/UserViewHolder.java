package com.github.overpass.gather.ui.meeting.chat.users.list.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.github.overpass.gather.R;
import com.github.overpass.gather.ui.meeting.chat.users.list.UsersAdapter;
import com.github.overpass.gather.ui.meeting.chat.users.model.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserViewHolder extends BaseViewHolder {

    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.ivUserPhoto)
    ImageView ivUserPhoto;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(UserModel user, UsersAdapter.OnItemClickListener listener) {
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        Glide.with(itemView)
                .load(user.getUri())
                .into(ivUserPhoto);
        itemView.setOnClickListener((view) -> listener.onItemClick(user.getId()));
    }
}