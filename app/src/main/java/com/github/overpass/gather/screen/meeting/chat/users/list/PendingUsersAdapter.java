package com.github.overpass.gather.screen.meeting.chat.users.list;

import android.os.Handler;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.annimon.stream.Stream;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.meeting.chat.users.list.viewholder.BaseViewHolder;
import com.github.overpass.gather.screen.meeting.chat.users.list.viewholder.PendingUserViewHolder;
import com.github.overpass.gather.screen.meeting.chat.users.list.viewholder.UserViewHolder;
import com.github.overpass.gather.screen.meeting.chat.users.model.UserModel;

public class PendingUsersAdapter extends UsersAdapter {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public PendingUsersAdapter(OnItemClickListener listener) {
        super(listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        bindIfNeeded(holder, position);
    }

    @Override
    protected UserViewHolder createUserViewHolder(@NonNull ViewGroup parent) {
        return createViewHolder(parent, R.layout.item_pending_user, PendingUserViewHolder::new);
    }

    public void setSwipeLocked(boolean locked) {
        String[] ids = Stream.of(users)
                .map(UserModel::getId)
                .toList()
                .toArray(new String[users.size()]);
        if (locked) {
            viewBinderHelper.lockSwipe(ids);
        } else {
            viewBinderHelper.unlockSwipe(ids);
        }
    }

    private void bindIfNeeded(BaseViewHolder holder, int position) {
        if (holder instanceof PendingUserViewHolder) {
            PendingUserViewHolder pendingUserViewHolder = (PendingUserViewHolder) holder;
            if (holder.itemView instanceof SwipeRevealLayout) {
                String id = users.get(position).getId();
                viewBinderHelper.bind((SwipeRevealLayout) holder.itemView, id);
            }
            new Handler().postDelayed(pendingUserViewHolder::updateMenu, 500);
        }
    }
}
