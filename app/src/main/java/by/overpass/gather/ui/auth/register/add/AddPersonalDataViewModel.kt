package by.overpass.gather.ui.auth.register.add

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import by.overpass.gather.commons.image.ChooseImageHelper
import by.overpass.gather.model.entity.splash.StartStatus
import by.overpass.gather.model.usecase.image.ImageSourceUseCase
import by.overpass.gather.model.usecase.login.StartStatusUseCase
import by.overpass.gather.model.usecase.userdata.PersonalDataUseCase
import by.overpass.gather.ui.base.personal.DataViewModel
import com.hadilq.liveevent.LiveEvent
import javax.inject.Inject

class AddPersonalDataViewModel @Inject constructor(
        application: Application,
        chooseImageHelper: ChooseImageHelper,
        personalDataUseCase: PersonalDataUseCase,
        chosenImageData: MutableLiveData<Uri>,
        writeDeniedData: LiveEvent<Boolean>,
        readDeniedData: LiveEvent<Boolean>,
        imageSourceUseCase: ImageSourceUseCase,
        private val startStatusUseCase: StartStatusUseCase
) : DataViewModel(
        application,
        chooseImageHelper,
        personalDataUseCase,
        chosenImageData,
        writeDeniedData,
        readDeniedData,
        imageSourceUseCase
) {

    fun setSignUpComplete() {
        startStatusUseCase.setStartStatus(StartStatus.AUTHORIZED)
    }
}
