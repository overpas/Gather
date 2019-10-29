package com.github.overpass.gather.di.image

import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.model.usecase.image.ImageSourceUseCase
import dagger.Module
import dagger.Provides

@Module
object ImageSourceModule {

    @Provides
    @ParentScope
    @JvmStatic
    fun provideImageSourceUseCase(): ImageSourceUseCase = ImageSourceUseCase()
}