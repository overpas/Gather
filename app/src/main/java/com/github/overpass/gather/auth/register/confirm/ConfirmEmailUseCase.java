package com.github.overpass.gather.auth.register.confirm;

import com.github.overpass.gather.SingleLiveEvent;

public class ConfirmEmailUseCase {

    private final ConfirmEmailRepo confirmEmailRepo;

    public ConfirmEmailUseCase(ConfirmEmailRepo confirmEmailRepo) {
        this.confirmEmailRepo = confirmEmailRepo;
    }

    public void confirmEmail(SingleLiveEvent<ConfirmEmailStatus> confirmEmailData) {
        confirmEmailRepo.confirmEmail(confirmEmailData);
    }
}
