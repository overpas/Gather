package com.github.overpass.gather.screen.meeting.chat

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.UIUtil.snackbar
import com.github.overpass.gather.screen.create.MeetingType
import com.github.overpass.gather.screen.dialog.ProgressDialogFragment
import com.github.overpass.gather.screen.dialog.delete.DeleteDialogFragment
import com.github.overpass.gather.screen.dialog.details.MeetingDetailsDialogFragment
import com.github.overpass.gather.screen.map.AuthUser
import com.github.overpass.gather.screen.meeting.base.BaseMeetingFragment
import com.github.overpass.gather.screen.meeting.base.LoadMeetingStatus
import com.github.overpass.gather.screen.meeting.chat.attachments.PhotosActivity
import com.github.overpass.gather.screen.meeting.chat.users.UsersActivity
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : BaseMeetingFragment<ChatViewModel>() {

    private var adapter: MessagesListAdapter<IMessageImpl>? = null

    override val layoutRes: Int = R.layout.fragment_chat

    override fun createViewModel(): ChatViewModel {
        return ViewModelProviders.of(activity!!).get(ChatViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupList()
        viewModel.messages(meetingId).observe(viewLifecycleOwner, Observer<MessageModel> { this.handleMessages(it) })
        input.setInputListener { input ->
            if (TextUtils.isEmpty(input)) {
                return@setInputListener false
            }
            viewModel.send(meetingId, input.toString())
            true
        }
        viewModel.checkUserRole(meetingId).observe(viewLifecycleOwner, Observer<AuthUser.Role> {
            this.handleRole(it)
        })
        viewModel.selectedItems().observe(viewLifecycleOwner, Observer<Int> {
            this.handleSelection(it)
        })
    }

    override fun handleLoadSuccess(success: LoadMeetingStatus.Success) {
        setMeetingTypeIcon(success.meetingAndRatio.meeting.type)
        tvMeetingName!!.text = success.meetingAndRatio.meeting.name
    }

    override fun handleLoadError(error: LoadMeetingStatus.Error) {
        snackbar(ivMeetingType, getString(R.string.couldnt_load_data))
    }

    private fun setupList() {
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                adapter = MessagesListAdapter(user.id) { imageView, url, payload ->
                    imageView.load(url)
                }
                messagesList!!.setAdapter(adapter)
                adapter!!.setOnMessageLongClickListener {
                    this@ChatFragment.handleOnLongClick(it)
                }
            }
        })
    }

    private fun handleOnLongClick(message: IMessageImpl) {
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
            if (user != null && message.user.id == user.id) {
                DeleteDialogFragment.show(meetingId, message.id, fragmentManager)
            }
        })
    }

    private fun setMeetingTypeIcon(type: Int) {
        val imageResource: Int
        if (MeetingType.isProtest(type)) {
            imageResource = R.drawable.ic_bund_large
        } else if (MeetingType.isEntertainment(type)) {
            imageResource = R.drawable.ic_beer_large
        } else {
            imageResource = R.drawable.ic_case_large
        }
        ivMeetingType!!.setImageResource(imageResource)
    }

    private fun setupToolbar() {
        toolbarChat.setNavigationOnClickListener { navIcon -> activity!!.finish() }
        toolbarChat.inflateMenu(R.menu.menu_chat)
        toolbarChat.setOnMenuItemClickListener { item -> false }
        toolbarChat.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_users) {
                UsersActivity.start(context, meetingId)
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_attachments) {
                PhotosActivity.start(context, meetingId)
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_details) {
                MeetingDetailsDialogFragment.show(meetingId, fragmentManager)
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_delete) {
                deleteMessages()
            }
            false
        }
    }

    private fun handleMessages(messageModel: MessageModel) {
        when (messageModel.tag()) {
            MessageModel.SUCCESS -> handleMessagesSuccess(messageModel.`as`(MessageModel.Success::class.java))
            MessageModel.ERROR -> handleMessagesError(messageModel.`as`(MessageModel.Error::class.java))
        }
    }

    private fun handleMessagesError(error: MessageModel.Error) {
        snackbar(tvMeetingName, error.throwable.localizedMessage)
    }

    private fun handleMessagesSuccess(success: MessageModel.Success) {
        setMessages(success.messages)
    }

    private fun setMessages(messages: List<IMessageImpl>) {
        if (adapter != null) {
            adapter!!.replace(messages)
        }
    }

    private fun handleRole(role: AuthUser.Role) {
        if (role == AuthUser.Role.ADMIN || role == AuthUser.Role.MODER) {
            adapter!!.enableSelectionMode {
                viewModel.setSelection(it)
            }
        }
    }

    private fun handleSelection(count: Int) {
        toolbarChat.menu.clear()
        if (count > 0) {
            toolbarChat.inflateMenu(R.menu.menu_chat_delete)
        } else {
            toolbarChat.inflateMenu(R.menu.menu_chat)
        }
    }

    private fun deleteMessages() {
        if (adapter != null) {
            val ids = adapter!!.selectedMessages
                    .map { it.id }
            viewModel.delete(meetingId, ids)
                    .observe(viewLifecycleOwner, Observer<DeleteStatus> {
                        this.handleDeletion(it)
                    })
        }
    }

    private fun handleDeletion(deleteStatus: DeleteStatus) {
        when (deleteStatus.tag()) {
            DeleteStatus.ERROR -> handleDeletionError(deleteStatus.`as`(DeleteStatus.Error::class.java))
            DeleteStatus.PROGRESS -> handleDeletionProgress(deleteStatus.`as`(DeleteStatus.Progress::class.java))
            DeleteStatus.SUCCESS -> handleDeletionSuccess(deleteStatus.`as`(DeleteStatus.Success::class.java))
        }
    }

    private fun handleDeletionSuccess(success: DeleteStatus.Success) {
        ProgressDialogFragment.hide(fragmentManager)
        setDefaultToolbar()
    }

    private fun setDefaultToolbar() {
        toolbarChat.menu.clear()
        toolbarChat.inflateMenu(R.menu.menu_chat)
    }

    private fun handleDeletionProgress(progress: DeleteStatus.Progress) {
        ProgressDialogFragment.show(fragmentManager)
    }

    private fun handleDeletionError(error: DeleteStatus.Error) {
        ProgressDialogFragment.hide(fragmentManager)
        snackbar(tvMeetingName, error.throwable.localizedMessage)
        setDefaultToolbar()
    }

    companion object {
        
        @JvmStatic
        fun newInstance(meetingId: String): ChatFragment {
            return newInstance(meetingId, ChatFragment())
        }
    }
}
