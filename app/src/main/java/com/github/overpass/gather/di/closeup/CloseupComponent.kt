package com.github.overpass.gather.di.closeup

import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.ui.meeting.chat.attachments.closeup.CloseupActivity
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [CloseupModule::class])
interface CloseupComponent {

    fun inject(closeupActivity: CloseupActivity)
}