package com.github.overpass.gather.di.meeting

import com.github.overpass.gather.di.meeting.chat.ChatComponent
import com.github.overpass.gather.di.meeting.join.JoinComponent
import com.github.overpass.gather.screen.meeting.MeetingActivity
import dagger.Subcomponent

@MeetingScope
@Subcomponent(modules = [MeetingModule::class])
interface MeetingComponent {

    fun getChatComponent(): ChatComponent

    fun getJoinComponent(): JoinComponent

    fun inject(meetingActivity: MeetingActivity)
}