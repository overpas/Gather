package com.github.overpass.gather.di.meeting.join

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.meeting.join.JoinViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class JoinModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(JoinViewModel::class)
    abstract fun bindJoinViewModel(joinViewModel: JoinViewModel): ViewModel
}