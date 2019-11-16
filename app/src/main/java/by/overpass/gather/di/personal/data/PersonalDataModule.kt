package by.overpass.gather.di.personal.data

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import by.overpass.gather.di.ViewScope
import dagger.Module
import dagger.Provides

@Module
object PersonalDataModule {

    @Provides
    @ViewScope
    @JvmStatic
    fun uriMutableLiveData(): MutableLiveData<Uri> = MutableLiveData()
}