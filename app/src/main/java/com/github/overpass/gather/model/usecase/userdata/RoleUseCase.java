package com.github.overpass.gather.model.usecase.userdata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.github.overpass.gather.model.commons.LiveDataUtils;
import com.github.overpass.gather.model.repo.meeting.UserRoleRepo;
import com.github.overpass.gather.model.repo.user.UserAuthRepo;
import com.github.overpass.gather.screen.map.AuthUser;

public class RoleUseCase {

    private final UserAuthRepo userAuthRepo;
    private final UserRoleRepo userRoleRepo;

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
