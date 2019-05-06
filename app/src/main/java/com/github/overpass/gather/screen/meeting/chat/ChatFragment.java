package com.github.overpass.gather.screen.meeting.chat;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingFragment;
import com.github.overpass.gather.screen.meeting.chat.users.UsersActivity;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;

import butterknife.BindView;

import static com.github.overpass.gather.model.commons.UIUtil.toast;

public class ChatFragment extends BaseMeetingFragment<ChatViewModel> {

    @BindView(R.id.input)
    MessageInput messageInput;
    @BindView(R.id.messagesList)
    MessagesList messagesList;
    @BindView(R.id.toolbarChat)
    Toolbar toolbarChat;

    @Override
    protected ChatViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ChatViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbarChat.setNavigationOnClickListener(navIcon -> getActivity().finish());
        toolbarChat.inflateMenu(R.menu.menu_chat);
        toolbarChat.setOnMenuItemClickListener(item -> false);
        toolbarChat.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_users) {
                UsersActivity.start(getContext(), getMeetingId());
                return true;
            } else if (item.getItemId() == R.id.action_attachments) {
                toast(ChatFragment.this, "Attachments");
                return true;
            }
            return false;
        });
    }

    public static ChatFragment newInstance(String meetingId) {
        return newInstance(meetingId, new ChatFragment());
    }
}
