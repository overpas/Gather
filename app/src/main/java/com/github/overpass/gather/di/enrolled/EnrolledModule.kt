package com.github.overpass.gather.di.enrolled

import androidx.lifecycle.ViewModel
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.di.app.vm.ViewModelKey
import com.github.overpass.gather.screen.meeting.enrolled.EnrolledViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EnrolledModule {

    @Binds
    @IntoMap
    @ViewScope
    @ViewModelKey(EnrolledViewModel::class)
    abstract fun bindEnrolledViewModel(viewModel: EnrolledViewModel): ViewModel
}