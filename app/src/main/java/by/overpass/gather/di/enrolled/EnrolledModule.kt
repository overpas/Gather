package by.overpass.gather.di.enrolled

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.meeting.enrolled.EnrolledViewModel
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