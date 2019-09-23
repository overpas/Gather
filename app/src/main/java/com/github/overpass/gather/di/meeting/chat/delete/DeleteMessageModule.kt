package com.github.overpass.gather.di.meeting.chat.delete

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.dialog.delete.DeleteMessageViewModel
import com.github.overpass.gather.screen.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DeleteMessageModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(DeleteMessageViewModel::class)
    abstract fun bindDeleteMessageViewModel(viewModel: DeleteMessageViewModel): ViewModel
}