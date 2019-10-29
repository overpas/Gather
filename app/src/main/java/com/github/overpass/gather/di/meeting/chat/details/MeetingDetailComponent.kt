package com.github.overpass.gather.di.meeting.chat.details

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.ui.dialog.details.MeetingDetailsDialogFragment
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [MeetingDetailModule::class])
interface MeetingDetailComponent {

    fun inject(meetingDetailsDialogFragment: MeetingDetailsDialogFragment)
}