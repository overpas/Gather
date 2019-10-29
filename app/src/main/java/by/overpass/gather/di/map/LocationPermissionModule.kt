package by.overpass.gather.di.map

import by.overpass.gather.di.ParentScope
import by.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import by.overpass.gather.model.usecase.permission.LocationPermissionUseCase
import by.overpass.gather.ui.map.PermissionRequestResult
import dagger.Module
import dagger.Provides

@Module
object LocationPermissionModule {

    @Provides
    @ParentScope
    @JvmStatic
    fun provideLocationPermissionUseCase(
            locationPermissionData: SingleLiveEvent<PermissionRequestResult>
    ): LocationPermissionUseCase = LocationPermissionUseCase(locationPermissionData)
}