package by.overpass.gather.di.map

import by.overpass.gather.di.ParentScope
import by.overpass.gather.model.usecase.permission.LocationPermissionUseCase
import com.hadilq.liveevent.LiveEvent
import dagger.Module
import dagger.Provides

@Module
object LocationPermissionModule {

    @Provides
    @ParentScope
    @JvmStatic
    fun provideLocationPermissionUseCase(): LocationPermissionUseCase = LocationPermissionUseCase(LiveEvent())
}