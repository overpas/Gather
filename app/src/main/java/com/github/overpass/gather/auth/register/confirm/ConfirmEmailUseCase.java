package com.github.overpass.gather.auth.register.confirm;

import androidx.lifecycle.LiveData;

class ConfirmEmailUseCase {

    private final ConfirmEmailRepo confirmEmailRepo;

    ConfirmEmailUseCase(ConfirmEmailRepo confirmEmailRepo) {
        this.confirmEmailRepo = confirmEmailRepo;
    }

    LiveData<ConfirmEmailStatus> confirmEmail() {
        return confirmEmailRepo.confirmEmail();
    }
}
