package com.github.overpass.gather.di.newmeeting

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.ui.create.NewMeetingActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [NewMeetingModule::class])
interface NewMeetingComponent {

    fun inject(newMeetingActivity: NewMeetingActivity)
}