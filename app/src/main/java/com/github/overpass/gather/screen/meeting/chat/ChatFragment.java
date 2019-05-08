package com.github.overpass.gather.screen.meeting.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.create.MeetingType;
import com.github.overpass.gather.screen.dialog.details.MeetingDetailsDialogFragment;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingFragment;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.github.overpass.gather.screen.meeting.chat.users.UsersActivity;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.List;

import butterknife.BindView;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;
import static com.github.overpass.gather.model.commons.UIUtil.toast;

public class ChatFragment extends BaseMeetingFragment<ChatViewModel> {

    @BindView(R.id.input)
    MessageInput messageInput;
    @BindView(R.id.messagesList)
    MessagesList messagesList;
    @BindView(R.id.toolbarChat)
    Toolbar toolbarChat;
    @BindView(R.id.ivMeetingType)
    ImageView ivMeetingType;
    @BindView(R.id.tvMeetingName)
    TextView tvMeetingName;

    MessagesListAdapter<IMessageImpl> adapter;

    @Override
    protected ChatViewModel createViewModel() {
        return ViewModelProviders.of(getActivity()).get(ChatViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        setupList();
        viewModel.messages(getMeetingId()).observe(getViewLifecycleOwner(), this::handleMessages);
        messageInput.setInputListener(input -> {
            if (TextUtils.isEmpty(input)) {
                return false;
            }
            viewModel.send(getMeetingId(), input.toString());
            return true;
        });
    }

    @Override
    protected void handleLoadSuccess(LoadMeetingStatus.Success success) {
        setMeetingTypeIcon(success.getMeetingAndRatio().getMeeting().getType());
        tvMeetingName.setText(success.getMeetingAndRatio().getMeeting().getName());
    }

    @Override
    protected void handleLoadError(LoadMeetingStatus.Error error) {
        snackbar(ivMeetingType, getString(R.string.couldnt_load_data));
    }

    private void setupList() {
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                adapter = new MessagesListAdapter<>(
                        user.getId(),
                        (imageView, url, payload) ->
                                Glide.with(ChatFragment.this)
                                        .load(url)
                                        .into(imageView)
                );
                messagesList.setAdapter(adapter);
            }
        });
    }

    private void setMeetingTypeIcon(int type) {
        int imageResource;
        if (MeetingType.isProtest(type)) {
            imageResource = R.drawable.ic_bund_large;
        } else if (MeetingType.isEntertainment(type)) {
            imageResource = R.drawable.ic_beer_large;
        } else {
            imageResource = R.drawable.ic_case_large;
        }
        ivMeetingType.setImageResource(imageResource);
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
                toast(ChatFragment.this, getString(R.string.attachments));
                return true;
            } else if (item.getItemId() == R.id.action_details) {
                MeetingDetailsDialogFragment.show(getMeetingId(), getFragmentManager());
                return true;
            }
            return false;
        });
    }

    private void handleMessages(MessageModel messageModel) {
        switch (messageModel.tag()) {
            case MessageModel.SUCCESS:
                handleMessagesSuccess(messageModel.as(MessageModel.Success.class));
                break;
            case MessageModel.ERROR:
                handleMessagesError(messageModel.as(MessageModel.Error.class));
                break;
        }
    }

    private void handleMessagesError(MessageModel.Error error) {
        snackbar(tvMeetingName, error.getThrowable().getLocalizedMessage());
    }

    private void handleMessagesSuccess(MessageModel.Success success) {
        setMessages(success.getMessages());
    }

    private void setMessages(List<IMessageImpl> messages) {
        if (adapter != null) {
            adapter.replace(messages);
        }
    }

    public static ChatFragment newInstance(String meetingId) {
        return newInstance(meetingId, new ChatFragment());
    }
}
