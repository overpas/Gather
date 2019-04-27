package com.github.overpass.gather.model.usecase.confirm;

import androidx.lifecycle.LiveData;

import com.github.overpass.gather.screen.auth.register.confirm.ConfirmEmailStatus;
import com.github.overpass.gather.model.repo.confirm.ConfirmEmailRepo;

public class ConfirmEmailUseCase {

    private final ConfirmEmailRepo confirmEmailRepo;

    public ConfirmEmailUseCase(ConfirmEmailRepo confirmEmailRepo) {
        this.confirmEmailRepo = confirmEmailRepo;
    }

    public LiveData<ConfirmEmailStatus> confirmEmail() {
        return confirmEmailRepo.confirmEmail();
    }
}
