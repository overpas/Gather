package com.github.overpass.gather.di.meeting

import android.util.Log
import androidx.lifecycle.Lifecycle
import com.github.overpass.gather.di.meeting.chat.ChatComponent
import com.github.overpass.gather.di.meeting.chat.ChatComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import com.github.overpass.gather.di.meeting.chat.users.UsersComponent
import com.github.overpass.gather.di.meeting.join.JoinComponent
import com.github.overpass.gather.model.commons.LifecycleDisposable
import com.github.overpass.gather.screen.meeting.MeetingActivity

class MeetingComponentManager(
        private val meetingComponent: MeetingComponent
) : MeetingComponent {

    init {
        Log.d(this::class.java.simpleName, "MeetingComponentManager Created")
    }

    private lateinit var chatComponentManagerDisposable: LifecycleDisposable<ChatComponentManager>

    override fun getChatComponent(): ChatComponent {
        throw IllegalStateException("The subcomponent hasn't been initialized. Consider calling" +
                "fun getChatComponent(lifecycle: Lifecycle) instead")
    }

    fun getChatComponent(lifecycle: Lifecycle): ChatComponent {
        chatComponentManagerDisposable = LifecycleDisposable(
                lifecycle,
                ChatComponentManager(meetingComponent.getChatComponent())
        )
        return chatComponentManagerDisposable.value!!
    }

    override fun getJoinComponent(): JoinComponent = meetingComponent.getJoinComponent()

    fun getUsersComponent(): UsersComponent =
            chatComponentManagerDisposable.value!!.getUsersComponent()

    fun getMeetingDetailComponent(): MeetingDetailComponent =
            chatComponentManagerDisposable.value!!.getMeetingDetailsComponent()

    fun getDeleteMessageComponent(): DeleteMessageComponent =
            chatComponentManagerDisposable.value!!.getDeleteMessageComponent()

    fun getAttachmentsComponent(lifecycle: Lifecycle): AttachmentsComponent =
            chatComponentManagerDisposable.value!!.getAttachmentsComponent(lifecycle)

    fun getAttachmentDetailComponent(): AttachmentsDetailsComponent =
            chatComponentManagerDisposable.value!!.getAttachmentDetailComponent()

    override fun inject(meetingActivity: MeetingActivity) = meetingComponent.inject(meetingActivity)

    protected fun finalize() {
        Log.d(this::class.java.simpleName, "MeetingComponentManager Destroyed")
    }
}