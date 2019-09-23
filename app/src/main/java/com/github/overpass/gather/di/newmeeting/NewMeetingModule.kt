package com.github.overpass.gather.di.newmeeting

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.create.NewMeetingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NewMeetingModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(NewMeetingViewModel::class)
    abstract fun bindNewMeetingViewModel(viewModel: NewMeetingViewModel): ViewModel
}