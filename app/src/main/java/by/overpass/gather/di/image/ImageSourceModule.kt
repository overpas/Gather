package by.overpass.gather.di.image

import by.overpass.gather.di.ParentScope
import by.overpass.gather.model.usecase.image.ImageSourceUseCase
import dagger.Module
import dagger.Provides

@Module
object ImageSourceModule {

    @Provides
    @ParentScope
    @JvmStatic
    fun provideImageSourceUseCase(): ImageSourceUseCase = ImageSourceUseCase()
}