package by.overpass.gather.ui.meeting.chat.users.list;

import android.os.Handler;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import by.overpass.gather.R;
import by.overpass.gather.ui.meeting.chat.users.list.viewholder.BaseViewHolder;
import by.overpass.gather.ui.meeting.chat.users.list.viewholder.PendingUserViewHolder;
import by.overpass.gather.ui.meeting.chat.users.list.viewholder.UserViewHolder;

import static by.overpass.gather.ui.meeting.chat.users.list.IdsKt.ids;

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
        String[] ids = ids(users);
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
