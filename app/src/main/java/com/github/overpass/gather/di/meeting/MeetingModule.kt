package com.github.overpass.gather.di.meeting

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.ui.meeting.MeetingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MeetingModule {

    @Binds
    @IntoMap
    @MeetingScope
    @ViewModelKey(MeetingViewModel::class)
    abstract fun bindMeetingViewModel(mapViewModel: MeetingViewModel): ViewModel
}