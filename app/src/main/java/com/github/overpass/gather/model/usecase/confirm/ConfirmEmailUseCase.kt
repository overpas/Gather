package com.github.overpass.gather.model.usecase.confirm

import androidx.lifecycle.LiveData

import com.github.overpass.gather.model.data.entity.confirm.ConfirmEmailStatus
import com.github.overpass.gather.model.repo.confirm.ConfirmEmailRepo

class ConfirmEmailUseCase(private val confirmEmailRepo: ConfirmEmailRepo) {

    fun confirmEmail(): LiveData<out ConfirmEmailStatus> {
        return confirmEmailRepo.confirmEmail()
    }
}
