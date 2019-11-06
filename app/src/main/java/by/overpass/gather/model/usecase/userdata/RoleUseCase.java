package by.overpass.gather.model.usecase.userdata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import by.overpass.gather.commons.android.lifecycle.LiveDataUtils;
import by.overpass.gather.data.repo.meeting.UserRoleRepo;
import by.overpass.gather.data.repo.user.UserAuthRepo;
import by.overpass.gather.ui.map.AuthUser;

import javax.inject.Inject;

public class RoleUseCase {

    private final UserAuthRepo userAuthRepo;
    private final UserRoleRepo userRoleRepo;

    @Inject
    public RoleUseCase(UserAuthRepo userAuthRepo, UserRoleRepo userRoleRepo) {
        this.userAuthRepo = userAuthRepo;
        this.userRoleRepo = userRoleRepo;
    }

    public LiveData<AuthUser.Role> getCurrentUserRole(String meetingId) {
        return Transformations.switchMap(
                userAuthRepo.getCurrentUser(),
                userData -> {
                    if (userData != null) {
                        return userRoleRepo.getUserRole(meetingId, userData.getId());
                    } else {
                        return LiveDataUtils.just(AuthUser.Role.NOBODY);
                    }
                }
        );
    }
}
