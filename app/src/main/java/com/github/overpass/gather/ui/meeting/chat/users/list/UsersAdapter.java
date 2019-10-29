package com.github.overpass.gather.ui.meeting.chat.users.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.overpass.gather.R;
import com.github.overpass.gather.ui.meeting.chat.users.list.viewholder.BaseViewHolder;
import com.github.overpass.gather.ui.meeting.chat.users.list.viewholder.NoDataViewHolder;
import com.github.overpass.gather.ui.meeting.chat.users.list.viewholder.ProgressViewHolder;
import com.github.overpass.gather.ui.meeting.chat.users.list.viewholder.UserViewHolder;
import com.github.overpass.gather.ui.meeting.chat.users.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_PROGRESS = 11;
    private static final int VIEW_TYPE_USER = 12;
    private static final int VIEW_TYPE_NO_DATA = 13;

    private final OnItemClickListener listener;
    protected final List<UserModel> users = new ArrayList<>();

    public UsersAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder;
        if (viewType == VIEW_TYPE_NO_DATA) {
            viewHolder = createViewHolder(parent, R.layout.item_no_data, NoDataViewHolder::new);
        } else if (viewType == VIEW_TYPE_PROGRESS) {
            viewHolder = createViewHolder(parent, R.layout.item_progress, ProgressViewHolder::new);
        } else {
            viewHolder = createUserViewHolder(parent);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(users.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = VIEW_TYPE_USER;
        if (users.get(position) == UserModel.PROGRESS) {
            viewType = VIEW_TYPE_PROGRESS;
        } else if (users.get(position) == UserModel.NO_DATA) {
            viewType = VIEW_TYPE_NO_DATA;
        }
        return viewType;
    }

    public void setUsers(@NonNull List<UserModel> newUsers) {
        Diff diff = new Diff(users, newUsers);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diff);
        users.clear();
        if (!newUsers.isEmpty()) {
            users.addAll(newUsers);
        } else {
            users.add(UserModel.NO_DATA);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    public void setProgress() {
        users.clear();
        users.add(UserModel.PROGRESS);
        notifyDataSetChanged();
    }

    protected <VH extends BaseViewHolder> VH createViewHolder(@NonNull ViewGroup parent,
                                            @LayoutRes int itemLayoutRes,
                                            Function<View, VH> creator) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(itemLayoutRes, parent, false);
        return creator.apply(view);
    }

    protected UserViewHolder createUserViewHolder(@NonNull ViewGroup parent) {
        return createViewHolder(parent, R.layout.item_user, UserViewHolder::new);
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
