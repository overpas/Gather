package com.github.overpass.gather.di.closeup

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.meeting.chat.attachments.closeup.CloseupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CloseupModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(CloseupViewModel::class)
    abstract fun bindCloseupViewModel(viewModel: CloseupViewModel): ViewModel
}
