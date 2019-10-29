package com.github.overpass.gather.di.map

import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.commons.android.lifecycle.SingleLiveEvent
import com.github.overpass.gather.model.usecase.permission.LocationPermissionUseCase
import com.github.overpass.gather.ui.map.PermissionRequestResult
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