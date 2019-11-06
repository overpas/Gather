package by.overpass.gather.model.usecase.confirm

import by.overpass.gather.data.repo.confirm.ConfirmEmailRepo
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class ConfirmEmailUseCase @Inject constructor(private val confirmEmailRepo: ConfirmEmailRepo) {

    fun confirmEmail(): Task<Void> = confirmEmailRepo.confirmEmail()

    fun isEmailVerified(): Boolean = confirmEmailRepo.isEmailVerified()
}
