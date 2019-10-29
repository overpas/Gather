package com.github.overpass.gather.di.meeting.chat.details

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.dialog.details.MeetingDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MeetingDetailModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(MeetingDetailsViewModel::class)
    abstract fun bindMeetingDetailsViewModel(viewModel: MeetingDetailsViewModel): ViewModel
}