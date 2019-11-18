package by.overpass.gather.ui.meeting.chat

import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.snackbar
import by.overpass.gather.ui.base.BaseFragmentKt
import by.overpass.gather.ui.create.MeetingType
import by.overpass.gather.ui.dialog.delete.DeleteDialogFragment
import by.overpass.gather.ui.dialog.details.MeetingDetailsDialogFragment
import by.overpass.gather.ui.dialog.progress.indeterminate.ProgressDialogFragment
import by.overpass.gather.ui.map.AuthUser
import by.overpass.gather.ui.meeting.base.LoadMeetingStatus
import by.overpass.gather.ui.meeting.chat.attachments.PhotosActivity
import by.overpass.gather.ui.meeting.chat.users.UsersActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : BaseFragmentKt<ChatViewModel>() {

    private var adapter: MessagesListAdapter<IMessageImpl>? = null

    override fun createViewModel(): ChatViewModel {
        return viewModelProvider.get(ChatViewModel::class.java)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_chat
    }

    override fun inject() {
        componentManager
                .getChatComponent(lifecycle)
                .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        setupList()
        viewModel.loadMeeting().observe(viewLifecycleOwner, Observer<LoadMeetingStatus> { this.handleLoadStatus(it) })
    }

    private fun handleLoadStatus(loadMeetingStatus: LoadMeetingStatus) {
        when (loadMeetingStatus.tag()) {
            LoadMeetingStatus.ERROR -> handleLoadError(loadMeetingStatus.`as`<LoadMeetingStatus.Error>(LoadMeetingStatus.Error::class.java))
            LoadMeetingStatus.PROGRESS -> handleProgress(loadMeetingStatus.`as`<LoadMeetingStatus.Progress>(LoadMeetingStatus.Progress::class.java))
            LoadMeetingStatus.SUCCESS -> handleLoadSuccess(loadMeetingStatus.`as`<LoadMeetingStatus.Success>(LoadMeetingStatus.Success::class.java))
        }
    }

    override fun onBind() {
        super.onBind()
        viewModel.messages().observe(viewLifecycleOwner, Observer<MessageModel> { this.handleMessages(it) })
        input.setInputListener { input ->
            if (TextUtils.isEmpty(input)) {
                return@setInputListener false
            }
            viewModel.send(input.toString())
            true
        }
        viewModel.checkUserRole().observe(viewLifecycleOwner, Observer<AuthUser.Role> { this.handleRole(it) })
        viewModel.selectedItems().observe(viewLifecycleOwner, Observer<Int> { this.handleSelection(it) })
    }

    private fun handleLoadSuccess(success: LoadMeetingStatus.Success) {
        setMeetingTypeIcon(success.meetingAndRatio.meeting.type)
        tvMeetingName!!.text = success.meetingAndRatio.meeting.name
    }

    private fun handleLoadError(error: LoadMeetingStatus.Error) {
        ivMeetingType!!.snackbar(R.string.couldnt_load_data, Snackbar.LENGTH_SHORT)
    }

    private fun handleProgress(progress: LoadMeetingStatus.Progress) {}

    private fun setupList() {
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                adapter = MessagesListAdapter(
                        user.id
                ) { imageView, url, payload ->
                    Glide.with(this@ChatFragment)
                            .load(url)
                            .into(imageView)
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
                DeleteDialogFragment.show(message.id, fragmentManager)
            }
        })
    }

    private fun setMeetingTypeIcon(type: Int) {
        ivMeetingType.setImageResource(if (MeetingType.isProtest(type)) {
            R.drawable.ic_bund_large
        } else if (MeetingType.isEntertainment(type)) {
            R.drawable.ic_beer_large
        } else {
            R.drawable.ic_case_large
        })
    }

    private fun setupToolbar() {
        toolbarChat.setNavigationOnClickListener { navIcon -> requireActivity().finish() }
        toolbarChat.inflateMenu(R.menu.menu_chat)
        toolbarChat.setOnMenuItemClickListener { item -> false }
        toolbarChat.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_users) {
                UsersActivity.start(requireContext())
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_attachments) {
                PhotosActivity.start(requireContext())
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_details) {
                MeetingDetailsDialogFragment.show(fragmentManager)
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_delete) {
                deleteMessages()
            }
            false
        }
    }

    private fun handleMessages(messageModel: MessageModel) {
        when (messageModel.tag()) {
            MessageModel.SUCCESS -> handleMessagesSuccess(messageModel.`as`<MessageModel.Success>(MessageModel.Success::class.java))
            MessageModel.ERROR -> handleMessagesError(messageModel.`as`<MessageModel.Error>(MessageModel.Error::class.java))
        }
    }

    private fun handleMessagesError(error: MessageModel.Error) {
        tvMeetingName!!.snackbar(error.throwable.localizedMessage, Snackbar.LENGTH_SHORT)
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
            val ids = adapter!!.selectedMessages.map { it.id }
            viewModel.delete(ids)
                    .observe(viewLifecycleOwner, Observer<DeleteStatus> { this.handleDeletion(it) })
        }
    }

    private fun handleDeletion(deleteStatus: DeleteStatus) {
        when (deleteStatus.tag()) {
            DeleteStatus.ERROR -> handleDeletionError(deleteStatus.`as`<DeleteStatus.Error>(DeleteStatus.Error::class.java))
            DeleteStatus.PROGRESS -> handleDeletionProgress(deleteStatus.`as`<DeleteStatus.Progress>(DeleteStatus.Progress::class.java))
            DeleteStatus.SUCCESS -> handleDeletionSuccess(deleteStatus.`as`<DeleteStatus.Success>(DeleteStatus.Success::class.java))
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
        tvMeetingName.snackbar(error.throwable.localizedMessage, Snackbar.LENGTH_SHORT)
        setDefaultToolbar()
    }

    companion object {

        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
}
