package com.github.overpass.gather.model.usecase.confirm

import com.github.overpass.gather.model.repo.confirm.ConfirmEmailRepo
import com.google.android.gms.tasks.Task

class ConfirmEmailUseCase(private val confirmEmailRepo: ConfirmEmailRepo) {

    fun confirmEmail(): Task<Void> = confirmEmailRepo.confirmEmail()

    fun isEmailVerified(): Boolean = confirmEmailRepo.isEmailVerified()
}
