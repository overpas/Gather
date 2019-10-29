package by.overpass.gather.di.meeting.join

import androidx.lifecycle.ViewModel
import by.overpass.gather.di.ViewScope
import by.overpass.gather.di.app.vm.ViewModelKey
import by.overpass.gather.ui.meeting.join.JoinViewModel
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