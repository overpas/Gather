package com.github.overpass.gather.ui.meeting.chat.users.list.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.overpass.gather.R;
import com.github.overpass.gather.ui.meeting.chat.users.list.UsersAdapter;
import com.github.overpass.gather.ui.meeting.chat.users.model.UserModel;

import butterknife.BindView;

public class PendingUserViewHolder extends UserViewHolder {

    @BindView(R.id.flMenuContainer)
    FrameLayout flMenuContainer;
    @BindView(R.id.tvAccept)
    TextView tvAccept;

    public PendingUserViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(UserModel user, UsersAdapter.OnItemClickListener listener) {
        super.bind(user, listener);
        tvAccept.setOnClickListener(view -> listener.onItemClick(user.getId()));
    }

    public void updateMenu() {
        ViewGroup.LayoutParams params = flMenuContainer.getLayoutParams();
        params.height = itemView.getHeight();
        flMenuContainer.setLayoutParams(params);
    }
}
