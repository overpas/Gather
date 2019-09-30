package com.github.overpass.gather.screen.meeting.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.annimon.stream.Stream;
import com.bumptech.glide.Glide;
import com.github.overpass.gather.App;
import com.github.overpass.gather.R;
import com.github.overpass.gather.screen.create.MeetingType;
import com.github.overpass.gather.screen.dialog.delete.DeleteDialogFragment;
import com.github.overpass.gather.screen.dialog.details.MeetingDetailsDialogFragment;
import com.github.overpass.gather.screen.dialog.progress.indeterminate.ProgressDialogFragment;
import com.github.overpass.gather.screen.map.AuthUser;
import com.github.overpass.gather.screen.meeting.base.BaseMeetingFragment;
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus;
import com.github.overpass.gather.screen.meeting.chat.attachments.PhotosActivity;
import com.github.overpass.gather.screen.meeting.chat.users.UsersActivity;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;

import static com.github.overpass.gather.model.commons.UIUtil.snackbar;

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

    private MessagesListAdapter<IMessageImpl> adapter;

    @NotNull
    @Override
    protected ChatViewModel createViewModel() {
        return getViewModelProvider().get(ChatViewModel.class);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void inject() {
        App.Companion.getComponentManager(this)
                .getChatComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        setupList();
    }

    @Override
    protected void onBind() {
        super.onBind();
        getViewModel().messages(getMeetingId()).observe(getViewLifecycleOwner(), this::handleMessages);
        messageInput.setInputListener(input -> {
            if (TextUtils.isEmpty(input)) {
                return false;
            }
            getViewModel().send(getMeetingId(), input.toString());
            return true;
        });
        getViewModel().checkUserRole(getMeetingId()).observe(getViewLifecycleOwner(), this::handleRole);
        getViewModel().selectedItems().observe(getViewLifecycleOwner(), this::handleSelection);
    }

    @Override
    protected void clearComponent() {
        super.clearComponent();
        App.Companion.getComponentManager(this)
                .clearChatComponent();
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
        getViewModel().getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                adapter = new MessagesListAdapter<>(
                        user.getId(),
                        (imageView, url, payload) ->
                                Glide.with(ChatFragment.this)
                                        .load(url)
                                        .into(imageView)
                );
                messagesList.setAdapter(adapter);
                adapter.setOnMessageLongClickListener(ChatFragment.this::handleOnLongClick);
            }
        });
    }

    private void handleOnLongClick(IMessageImpl message) {
        getViewModel().getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null && message.getUser().getId().equals(user.getId())) {
                DeleteDialogFragment.show(getMeetingId(), message.getId(), getFragmentManager());
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
        toolbarChat.setNavigationOnClickListener(navIcon -> requireActivity().finish());
        toolbarChat.inflateMenu(R.menu.menu_chat);
        toolbarChat.setOnMenuItemClickListener(item -> false);
        toolbarChat.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_users) {
                UsersActivity.Companion.start(requireContext(), getMeetingId());
                return true;
            } else if (item.getItemId() == R.id.action_attachments) {
                PhotosActivity.Companion.start(requireContext(), getMeetingId());
                return true;
            } else if (item.getItemId() == R.id.action_details) {
                MeetingDetailsDialogFragment.show(getMeetingId(), getFragmentManager());
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                deleteMessages();
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

    private void handleRole(AuthUser.Role role) {
        if (role == AuthUser.Role.ADMIN || role == AuthUser.Role.MODER) {
            adapter.enableSelectionMode(getViewModel()::setSelection);
        }
    }

    private void handleSelection(int count) {
        toolbarChat.getMenu().clear();
        if (count > 0) {
            toolbarChat.inflateMenu(R.menu.menu_chat_delete);
        } else {
            toolbarChat.inflateMenu(R.menu.menu_chat);
        }
    }

    private void deleteMessages() {
        if (adapter != null) {
            List<String> ids = Stream.of(adapter.getSelectedMessages())
                    .map(IMessageImpl::getId)
                    .toList();
            getViewModel().delete(getMeetingId(), ids)
                    .observe(getViewLifecycleOwner(), this::handleDeletion);
        }
    }

    private void handleDeletion(DeleteStatus deleteStatus) {
        switch (deleteStatus.tag()) {
            case DeleteStatus.ERROR:
                handleDeletionError(deleteStatus.as(DeleteStatus.Error.class));
                break;
            case DeleteStatus.PROGRESS:
                handleDeletionProgress(deleteStatus.as(DeleteStatus.Progress.class));
                break;
            case DeleteStatus.SUCCESS:
                handleDeletionSuccess(deleteStatus.as(DeleteStatus.Success.class));
                break;
        }
    }

    private void handleDeletionSuccess(DeleteStatus.Success success) {
        ProgressDialogFragment.Companion.hide(getFragmentManager());
        setDefaultToolbar();
    }

    private void setDefaultToolbar() {
        toolbarChat.getMenu().clear();
        toolbarChat.inflateMenu(R.menu.menu_chat);
    }

    private void handleDeletionProgress(DeleteStatus.Progress progress) {
        ProgressDialogFragment.Companion.show(getFragmentManager());
    }

    private void handleDeletionError(DeleteStatus.Error error) {
        ProgressDialogFragment.Companion.hide(getFragmentManager());
        snackbar(tvMeetingName, error.getThrowable().getLocalizedMessage());
        setDefaultToolbar();
    }

    public static ChatFragment newInstance(String meetingId) {
        return newInstance(meetingId, new ChatFragment());
    }
}
