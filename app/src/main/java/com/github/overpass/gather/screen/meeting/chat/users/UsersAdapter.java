package com.github.overpass.gather.screen.meeting.chat.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.overpass.gather.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final OnItemClickListener listener;

    private final List<UserModel> users = new ArrayList<>();

    public UsersAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<UserModel> newUsers) {
        Diff diff = new Diff(users, newUsers);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diff);
        users.clear();
        users.addAll(newUsers);
        diffResult.dispatchUpdatesTo(this);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvEmail)
        TextView tvEmail;
        @BindView(R.id.ivUserPhoto)
        ImageView ivUserPhoto;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(UserModel user, OnItemClickListener listener) {
            tvUsername.setText(user.getUsername());
            tvEmail.setText(user.getEmail());
            Glide.with(itemView)
                    .load(user.getUri())
                    .into(ivUserPhoto);
            itemView.setOnClickListener((view) -> listener.onItemClick(user.getId()));
        }
    }

    private static class Diff extends DiffUtil.Callback {

        private final List<UserModel> oldList;
        private final List<UserModel> newList;

        public Diff(List<UserModel> oldList, List<UserModel> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPos, int newItemPos) {
            return oldList.get(oldItemPos).getId().equals(newList.get(newItemPos).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }

    public interface OnItemClickListener {

        void onItemClick(String id);
    }
}
